package dev.whyoleg.foreign.index.cx.generator.info

import dev.whyoleg.foreign.index.cx.*
import dev.whyoleg.foreign.index.cx.clang.*
import dev.whyoleg.foreign.index.cx.generator.*
import dev.whyoleg.foreign.index.cx.generator.internal.*
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
