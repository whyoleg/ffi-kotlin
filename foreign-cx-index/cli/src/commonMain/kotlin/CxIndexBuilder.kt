package dev.whyoleg.foreign.cx.index.cli

import dev.whyoleg.foreign.cx.index.*
import dev.whyoleg.foreign.cx.index.clang.*
import dev.whyoleg.foreign.cx.index.cli.info.*
import dev.whyoleg.foreign.cx.index.cli.internal.*
import kotlinx.cinterop.*

class CxIndexBuilder {
    private class DeclarationRegistry(val name: CxHeaderName) {
        val functions = mutableMapOf<CxDeclarationId, CxFunctionInfo>()
        val typedefs = mutableMapOf<CxDeclarationId, CxTypedefInfo>()
        val records = mutableMapOf<CxDeclarationId, CxRecordInfo>()
        val enums = mutableMapOf<CxDeclarationId, CxEnumInfo>()
    }

    private val all = DeclarationRegistry(CxHeaderName(""))
    private val headerByPath = mutableMapOf<String?, DeclarationRegistry>(
        null to DeclarationRegistry(CxHeaderName.BuiltIn)
    )

    private val dummyRecord = CxRecordInfo(
        id = CxDeclarationId(""),
        name = null,
        size = Long.MIN_VALUE,
        align = Long.MIN_VALUE,
        isUnion = false,
        fields = emptyList()
    )

    fun function(cursor: CValue<CXCursor>): CxDeclarationId = save(DeclarationRegistry::functions, cursor, null, ::buildFunctionInfo)
    fun typedef(cursor: CValue<CXCursor>): CxDeclarationId = save(DeclarationRegistry::typedefs, cursor, null, ::buildTypedefInfo)
    fun record(cursor: CValue<CXCursor>): CxDeclarationId = save(DeclarationRegistry::records, cursor, dummyRecord, ::buildRecordInfo)
    fun enum(cursor: CValue<CXCursor>): CxDeclarationId = save(DeclarationRegistry::enums, cursor, null, ::buildEnumInfo)

    fun include(name: String, path: String) {
        headerByPath[path] = DeclarationRegistry(CxHeaderName(name))
    }

    private inline fun <T : CxDeclarationInfo> save(
        getDeclarations: (DeclarationRegistry) -> MutableMap<CxDeclarationId, T>,
        cursor: CValue<CXCursor>,
        dummy: T?,
        block: (id: CxDeclarationId, name: CxDeclarationName?, cursor: CValue<CXCursor>) -> T,
    ): CxDeclarationId {
        val id = clang_getCursorUSR(cursor).useString().let {
            checkNotNull(it) { "USR = null" }
            check(it.isNotBlank()) { "USR is blank" }
            CxDeclarationId(it)
        }
        val allDeclarations = getDeclarations(all)
        if (id in allDeclarations) return id

        val name = when (clang_Cursor_isAnonymous(cursor)) {
            1U   -> null
            else -> CxDeclarationName(cursor.spelling)
        }
        val info = when (dummy) {
            null -> block(id, name, cursor)
            else -> {
                //save dummy to be able to reference it in recursive records
                allDeclarations[id] = dummy
                try {
                    block(id, name, cursor)
                } catch (cause: Throwable) {
                    //drop dummy if failed
                    allDeclarations.remove(id)
                    throw cause
                }
            }
        }
        val headerDeclarations = getDeclarations(
            headerByPath.getValue(clang_getFileName(cursor.locationFile).useString())
        )
        headerDeclarations[id] = info
        allDeclarations[id] = info
        return id
    }

    fun build(): CxIndex {
        fun DeclarationRegistry.cx(): CxHeaderInfo = CxHeaderInfo(
            name = name,
            typedefs = typedefs.values.toList(),
            records = records.values.toList(),
            enums = enums.values.toList(),
            functions = functions.values.toList()
        )

        return CxIndex(headers = headerByPath.map { it.value.cx() }.filter(CxHeaderInfo::isNotEmpty))
    }
}
