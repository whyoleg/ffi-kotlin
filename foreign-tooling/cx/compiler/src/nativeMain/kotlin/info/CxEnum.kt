package dev.whyoleg.foreign.tooling.cx.compiler.info

import dev.whyoleg.foreign.tooling.cx.compiler.*
import dev.whyoleg.foreign.tooling.cx.compiler.internal.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import kotlinx.cinterop.*

internal fun CxIndexBuilder.buildEnumInfo(
    id: CxDeclarationId,
    name: CxDeclarationName?,
    cursor: CValue<CXCursor>,
): CxEnumInfo = CxEnumInfo(
    id = id,
    name = name,
    constants = buildList {
        visitChildren(cursor) { constantCursor, _ ->
            add(
                CxEnumInfo.Constant(
                    name = constantCursor.spelling,
                    value = clang_getEnumConstantDeclValue(constantCursor)
                )
            )
            CXChildVisitResult.CXChildVisit_Continue
        }
    }
)
