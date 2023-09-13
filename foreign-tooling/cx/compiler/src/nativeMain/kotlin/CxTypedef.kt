package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import kotlinx.cinterop.*

internal fun CxIndexBuilder.buildTypedef(
    id: CxDeclarationId,
    name: CxDeclarationName?,
    headerName: CxHeaderName?,
    cursor: CValue<CXCursor>,
): CxTypedef = CxTypedef(
    id = id,
    name = checkNotNull(name) { "typedef can not be unnamed" },
    headerName = headerName,
    aliased = buildType(clang_getTypedefDeclUnderlyingType(cursor))
)
