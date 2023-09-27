package dev.whyoleg.foreign.tooling.cx.bridge.model

import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import kotlinx.serialization.*

@Serializable
public data class CxBridgeFragmentMapping(
    val fragmentId: CxBridgeFragmentId,
    val variables: Map<CxBridgeDeclarationId, CxCompilerDeclarationId>,
    val enums: Map<CxBridgeDeclarationId, CxCompilerDeclarationId>,
    val records: Map<CxBridgeDeclarationId, CxCompilerDeclarationId>,
    val typedefs: Map<CxBridgeDeclarationId, CxCompilerDeclarationId>,
    val functions: Map<CxBridgeDeclarationId, CxCompilerDeclarationId>,
)
