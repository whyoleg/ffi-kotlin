package dev.whyoleg.foreign.cx.index.generator.info

import dev.whyoleg.foreign.cx.index.*
import dev.whyoleg.foreign.cx.index.clang.*
import dev.whyoleg.foreign.cx.index.clang.CXCursorKind.*
import dev.whyoleg.foreign.cx.index.generator.*
import dev.whyoleg.foreign.cx.index.generator.internal.*
import kotlinx.cinterop.*

internal fun CxIndexBuilder.buildRecordInfo(
    id: CxDeclarationId,
    name: CxDeclarationName?,
    cursor: CValue<CXCursor>,
): CxRecordInfo {
    //more info on size values https://clang.llvm.org/doxygen/group__CINDEX__TYPES.html#gaaf1b95e9e7e792a08654563fef7502c1
    val size = clang_Type_getSizeOf(cursor.type)
    return CxRecordInfo(
        id = id,
        name = name,
        members = when {
            size > 0 -> {
                val align = clang_Type_getAlignOf(cursor.type)
                check(align > 0) { "wrong alignOf result: $align" }
                CxRecordInfo.Members(
                    size = size,
                    align = align,
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
            }
            size == CXTypeLayoutError_Incomplete.toLong() -> null // opaque
            else -> error("wrong sizeOf result: $size")
        },
        isUnion = when (val kind = clang_getCursorKind(cursor)) {
            CXCursor_StructDecl -> false
            CXCursor_UnionDecl  -> true
            else                -> error("Wrong kind for record: $kind")
        },
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
