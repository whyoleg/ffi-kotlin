package dev.whyoleg.ffi.c.index

import dev.whyoleg.ffi.c.index.clang.*
import kotlinx.cinterop.*

public data class CFunctionInfo
internal constructor(
    val name: String?,
    val returnType: CTypeInfo,
    val parameters: List<Parameter>,
) {
    public data class Parameter
    internal constructor(
        val name: String?,
        val type: CTypeInfo,
    )
}

internal fun CIndexRegistry.parseFunctionInfo(
    cursor: CValue<CXCursor>,
): CFunctionInfo = Logger.logging("FUNCTION", cursor.spelling, skipLogging = true) {
    CFunctionInfo(
        name = cursor.spelling,
        returnType = parseTypeInfo(clang_getCursorResultType(cursor)),
        parameters = buildList {
            repeat(clang_Cursor_getNumArguments(cursor)) { i ->
                val argCursor = clang_Cursor_getArgument(cursor, i.convert())
                add(
                    CFunctionInfo.Parameter(
                        name = argCursor.spelling,
                        type = parseTypeInfo(argCursor.type)
                    )
                )
            }
        }
    )
}
