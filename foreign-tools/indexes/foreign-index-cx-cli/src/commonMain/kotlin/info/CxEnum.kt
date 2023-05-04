package dev.whyoleg.foreign.index.cx.cli.info

import dev.whyoleg.foreign.index.cx.*
import dev.whyoleg.foreign.index.cx.clang.*
import dev.whyoleg.foreign.index.cx.cli.*
import dev.whyoleg.foreign.index.cx.cli.internal.*
import kotlinx.cinterop.*

fun CxIndexBuilder.buildEnumInfo(
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
