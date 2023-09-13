package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.internal.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import kotlinx.cinterop.*

internal fun CxIndexBuilder.buildEnum(
    id: CxDeclarationId,
    name: CxDeclarationName?,
    headerName: CxHeaderName?,
    cursor: CValue<CXCursor>,
): CxEnum = CxEnum(
    id = id,
    name = name,
    headerName = headerName,
    constants = buildList {
        visitChildren(cursor) { constantCursor, _ ->
            add(
                CxEnum.Constant(
                    name = constantCursor.spelling,
                    value = clang_getEnumConstantDeclValue(constantCursor)
                )
            )
            CXChildVisitResult.CXChildVisit_Continue
        }
    }
)
