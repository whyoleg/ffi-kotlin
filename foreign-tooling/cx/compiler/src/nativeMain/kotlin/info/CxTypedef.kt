package dev.whyoleg.foreign.tooling.cx.compiler.info

import dev.whyoleg.foreign.tooling.cx.compiler.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*
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
