package dev.whyoleg.foreign.cx.index.cli.info

import dev.whyoleg.foreign.cx.index.*
import dev.whyoleg.foreign.cx.index.clang.*
import dev.whyoleg.foreign.cx.index.cli.*
import dev.whyoleg.foreign.cx.index.cli.internal.*
import kotlinx.cinterop.*

fun CxIndexBuilder.buildStructInfo(
    id: CxDeclarationId,
    name: CxDeclarationName,
    cursor: CValue<CXCursor>,
): CxStructInfo = CxStructInfo(
    id = id,
    name = name,
    size = clang_Type_getSizeOf(cursor.type),
    align = clang_Type_getAlignOf(cursor.type),
    fields = buildList {
        visitFields(cursor.type) { fieldCursor ->
            add(
                CxStructInfo.Field(
                    name = fieldCursor.spelling,
                    type = buildTypeInfo(fieldCursor.type)
                )
            )
            CXVisitorResult.CXVisit_Continue
        }
    }
)

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
