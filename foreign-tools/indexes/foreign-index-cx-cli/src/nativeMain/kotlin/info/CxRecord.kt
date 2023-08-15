package dev.whyoleg.foreign.index.cx.cli.info

import dev.whyoleg.foreign.index.cx.*
import dev.whyoleg.foreign.index.cx.clang.*
import dev.whyoleg.foreign.index.cx.clang.CXCursorKind.*
import dev.whyoleg.foreign.index.cx.cli.*
import dev.whyoleg.foreign.index.cx.cli.internal.*
import kotlinx.cinterop.*

fun CxIndexBuilder.buildRecordInfo(
    id: CxDeclarationId,
    name: CxDeclarationName?,
    cursor: CValue<CXCursor>,
): CxRecordInfo = CxRecordInfo(
    id = id,
    name = name,
    size = clang_Type_getSizeOf(cursor.type),
    align = clang_Type_getAlignOf(cursor.type),
    isUnion = when (val kind = clang_getCursorKind(cursor)) {
        CXCursor_StructDecl -> false
        CXCursor_UnionDecl  -> true
        else                -> error("Wrong kind for record: $kind")
    },
    fields = buildList {
        visitFields(cursor.type) { fieldCursor ->
            add(
                CxRecordInfo.Field(
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
