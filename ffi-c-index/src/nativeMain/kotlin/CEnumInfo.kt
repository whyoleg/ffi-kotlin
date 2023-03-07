package dev.whyoleg.ffi.c.index

import dev.whyoleg.ffi.c.index.clang.*
import kotlinx.cinterop.*

public data class CEnumInfo
internal constructor(
    val name: String?,
    val constants: List<Constant>,
) {
    public data class Constant
    internal constructor(
        val name: String?,
        val value: Long,
    )
}

internal fun CIndexRegistry.parseEnumInfo(
    cursor: CValue<CXCursor>,
): CEnumInfo = Logger.logging("ENUM", cursor.spelling, skipLogging = true) {
    CEnumInfo(
        name = cursor.spelling,
        constants = buildList {
            visitChildren(cursor) { constantCursor, _ ->
                add(
                    CEnumInfo.Constant(
                        name = constantCursor.spelling,
                        value = clang_getEnumConstantDeclValue(constantCursor)
                    )
                )
                CXChildVisitResult.CXChildVisit_Continue
            }
        }
    )
}
