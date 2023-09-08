package dev.whyoleg.foreign.cx.index.generator.info

import dev.whyoleg.foreign.cx.index.*
import dev.whyoleg.foreign.cx.index.clang.*
import dev.whyoleg.foreign.cx.index.generator.*
import dev.whyoleg.foreign.cx.index.generator.internal.*
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
