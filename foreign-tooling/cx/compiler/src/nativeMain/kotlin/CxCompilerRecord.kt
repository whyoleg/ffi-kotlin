package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.internal.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.CXCursorKind.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import kotlinx.cinterop.*

internal fun CxCompilerIndexBuilder.buildRecord(
    id: CxCompilerDeclarationId,
    declarationName: String?,
    headerName: String?,
    cursor: CValue<CXCursor>,
): CxCompilerRecord {
    //more info on size values https://clang.llvm.org/doxygen/group__CINDEX__TYPES.html#gaaf1b95e9e7e792a08654563fef7502c1
    val size = clang_Type_getSizeOf(cursor.type)
    return CxCompilerRecord(
        id = id,
        declarationName = declarationName,
        headerName = headerName,
        members = when {
            size > 0 -> {
                val align = clang_Type_getAlignOf(cursor.type)
                check(align > 0) { "wrong alignOf result: $align" }
                CxCompilerRecord.Members(
                    size = size,
                    align = align,
                    fields = buildList {
                        visitFields(cursor.type) { fieldCursor ->
                            add(
                                CxCompilerRecord.Field(
                                    name = fieldCursor.spelling,
                                    type = buildType(fieldCursor.type)
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
