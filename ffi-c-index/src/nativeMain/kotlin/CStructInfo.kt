package dev.whyoleg.ffi.c.index

import dev.whyoleg.ffi.c.index.clang.*
import kotlinx.cinterop.*

public data class CStructInfo
internal constructor(
    val name: String?,
    val size: Long, //TODO: what is -2?
    val align: Long,
    val fields: List<Field>,
) {
    public data class Field
    internal constructor(
        val name: String?,
        val type: CTypeInfo,
    )
}

internal fun CIndexRegistry.parseStructInfo(
    cursor: CValue<CXCursor>,
): CStructInfo = Logger.logging("STRUCT", cursor.spelling, skipLogging = true) {
    val type = cursor.type
    CStructInfo(
        name = cursor.spelling,
        size = clang_Type_getSizeOf(type),
        align = clang_Type_getAlignOf(type),
        fields = buildList {
            visitFields(type) { fieldCursor ->
                add(
                    CStructInfo.Field(
                        name = fieldCursor.spelling,
                        type = parseTypeInfo(fieldCursor.type)
                    )
                )
                CXVisitorResult.CXVisit_Continue
            }
        }
    )
}

private typealias CursorFieldVisitor = (fieldCursor: CValue<CXCursor>) -> CXVisitorResult

private fun visitFields(type: CValue<CXType>, visitor: CursorFieldVisitor) {
    val visitorRef = StableRef.create(visitor)
    try {
        clang_Type_visitFields(type, staticCFunction { cursor, clientData ->
            clientData!!.asStableRef<CursorFieldVisitor>().get().invoke(cursor)
        }, visitorRef.asCPointer())
    } finally {
        visitorRef.dispose()
    }
}
