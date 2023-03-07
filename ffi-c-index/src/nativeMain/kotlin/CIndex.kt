package dev.whyoleg.ffi.c.index

import dev.whyoleg.ffi.c.index.clang.*
import kotlinx.cinterop.*

public value class USR
internal constructor(
    public val value: String,
)

public data class IncludeAwareInfo<T>
internal constructor(
    val include: String?,
    val info: T,
)

public typealias IncludeAwareMap<T> = Map<USR, IncludeAwareInfo<T>>

public sealed interface CIndex {
    public val functions: IncludeAwareMap<CFunctionInfo>
    public val typedefs: IncludeAwareMap<CTypedefInfo>
    public val structs: IncludeAwareMap<CStructInfo>
    public val enums: IncludeAwareMap<CEnumInfo>
}

internal sealed interface CIndexRegistry {
    fun function(cursor: CValue<CXCursor>): USR
    fun typedef(cursor: CValue<CXCursor>): USR
    fun struct(cursor: CValue<CXCursor>): USR
    fun enum(cursor: CValue<CXCursor>): USR

    fun include(name: String, path: String)
}

internal class CIndexImpl : CIndex, CIndexRegistry {
    override val functions = mutableMapOf<USR, IncludeAwareInfo<CFunctionInfo>>()
    override val typedefs = mutableMapOf<USR, IncludeAwareInfo<CTypedefInfo>>()
    override val structs = mutableMapOf<USR, IncludeAwareInfo<CStructInfo>>()
    override val enums = mutableMapOf<USR, IncludeAwareInfo<CEnumInfo>>()

    private val includeMap = mutableMapOf<String, String>()

    private val dummyStruct = IncludeAwareInfo("", CStructInfo(null, Long.MIN_VALUE, Long.MIN_VALUE, emptyList()))

    override fun include(name: String, path: String) {
        includeMap[path] = name
    }

    private inline fun <T : Any> MutableMap<USR, IncludeAwareInfo<T>>.save(
        cursor: CValue<CXCursor>,
        dummy: IncludeAwareInfo<T>?,
        block: (cursor: CValue<CXCursor>) -> T,
    ): USR {
        val rawUSR = clang_getCursorUSR(cursor).useString()
        checkNotNull(rawUSR) { "USR = null" }
        check(rawUSR.isNotBlank()) { "USR is blank" }

        val usr = USR(rawUSR)

        if (usr !in this) {
            if (dummy != null) this[usr] = dummy //save dummy to be able to reference it in recursive structs
            try {
                this[usr] = IncludeAwareInfo(
                    include = clang_getFileName(cursor.locationFile).useString()?.let(includeMap::get),
                    info = block(cursor)
                )
            } catch (cause: Throwable) {
                this.remove(usr) //drop dummy if failed
                throw cause
            }

        }
        return usr
    }

    override fun function(cursor: CValue<CXCursor>): USR = functions.save(cursor, null, ::parseFunctionInfo)
    override fun typedef(cursor: CValue<CXCursor>): USR = typedefs.save(cursor, null, ::parseTypedefInfo)
    override fun struct(cursor: CValue<CXCursor>): USR = structs.save(cursor, dummyStruct, ::parseStructInfo)
    override fun enum(cursor: CValue<CXCursor>): USR = enums.save(cursor, null, ::parseEnumInfo)
}
