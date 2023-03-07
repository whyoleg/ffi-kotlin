package dev.whyoleg.ffi.c.index

import dev.whyoleg.ffi.c.index.clang.*
import kotlinx.cinterop.*

public data class CTypedefInfo
internal constructor(
    val name: String?,
    val aliased: CTypeInfo,
)

internal fun CIndexRegistry.parseTypedefInfo(
    cursor: CValue<CXCursor>,
): CTypedefInfo = Logger.logging("TYPEDEF", cursor.spelling, skipLogging = true) {
    CTypedefInfo(
        name = cursor.spelling,
        aliased = parseTypeInfo(clang_getTypedefDeclUnderlyingType(cursor))
    )
}
