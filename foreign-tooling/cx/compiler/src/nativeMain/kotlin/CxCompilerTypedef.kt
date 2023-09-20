package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import kotlinx.cinterop.*

internal fun CxCompilerIndexBuilder.buildTypedef(
    id: CxCompilerDeclarationId,
    declarationName: String?,
    headerName: String?,
    cursor: CValue<CXCursor>,
): CxCompilerTypedef = CxCompilerTypedef(
    id = id,
    declarationName = checkNotNull(declarationName) { "typedef can not be unnamed" },
    headerName = headerName,
    aliased = buildType(clang_getTypedefDeclUnderlyingType(cursor))
)
