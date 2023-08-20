package dev.whyoleg.foreign.index.cx.generator.info

import dev.whyoleg.foreign.index.cx.*
import dev.whyoleg.foreign.index.cx.clang.*
import dev.whyoleg.foreign.index.cx.generator.*
import kotlinx.cinterop.*

internal fun CxIndexBuilder.buildTypedefInfo(
    id: CxDeclarationId,
    name: CxDeclarationName?,
    cursor: CValue<CXCursor>,
): CxTypedefInfo = CxTypedefInfo(
    id = id,
    name = checkNotNull(name) { "typedef can not be unnamed" },
    aliased = buildTypeInfo(clang_getTypedefDeclUnderlyingType(cursor))
)
