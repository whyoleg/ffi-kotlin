package dev.whyoleg.foreign.cx.index.cli.info

import dev.whyoleg.foreign.cx.index.*
import dev.whyoleg.foreign.cx.index.clang.*
import dev.whyoleg.foreign.cx.index.cli.*
import kotlinx.cinterop.*

fun CxIndexBuilder.buildTypedefInfo(
    id: CxDeclarationId,
    name: CxDeclarationName?,
    cursor: CValue<CXCursor>,
): CxTypedefInfo = CxTypedefInfo(
    id = id,
    name = checkNotNull(name) { "typedef can not be unnamed" },
    aliased = buildTypeInfo(clang_getTypedefDeclUnderlyingType(cursor))
)
