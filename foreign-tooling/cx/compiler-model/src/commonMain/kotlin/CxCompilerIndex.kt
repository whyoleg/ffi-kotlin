package dev.whyoleg.foreign.tooling.cx.compiler.model

import kotlinx.serialization.*

@Serializable
public data class CxCompilerIndex(
    val typedefs: Map<CxCompilerDeclarationId, CxCompilerTypedef>,
    val enums: Map<CxCompilerDeclarationId, CxCompilerEnum>,
    val records: Map<CxCompilerDeclarationId, CxCompilerRecord>,
    val functions: Map<CxCompilerDeclarationId, CxCompilerFunction>,
)
