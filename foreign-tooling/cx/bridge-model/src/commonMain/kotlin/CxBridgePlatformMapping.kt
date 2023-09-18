package dev.whyoleg.foreign.tooling.cx.bridge.model

import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import kotlinx.serialization.*

// per final kotlin target
// TODO: naming
@Serializable
public data class CxBridgePlatformMapping(
    val name: String,
    val index: CxIndex,
    val typedefs: Map<CxBridgeDeclarationId, CxDeclarationId>,
    val enums: Map<CxBridgeDeclarationId, CxDeclarationId>,
    val records: Map<CxBridgeDeclarationId, CxDeclarationId>,
    val functions: Map<CxBridgeDeclarationId, CxDeclarationId>,
)
