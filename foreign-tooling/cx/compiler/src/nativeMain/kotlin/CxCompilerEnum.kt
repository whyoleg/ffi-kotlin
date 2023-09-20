package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.internal.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import kotlinx.cinterop.*

internal fun CxCompilerIndexBuilder.buildEnum(
    id: CxCompilerDeclarationId,
    declarationName: String?,
    headerName: String?,
    cursor: CValue<CXCursor>,
): CxCompilerEnum = CxCompilerEnum(
    id = id,
    declarationName = declarationName,
    headerName = headerName,
    constants = buildList {
        visitChildren(cursor) { constantCursor, _ ->
            add(
                CxCompilerEnum.Constant(
                    name = constantCursor.spelling,
                    value = clang_getEnumConstantDeclValue(constantCursor)
                )
            )
            CXChildVisitResult.CXChildVisit_Continue
        }
    }
)
