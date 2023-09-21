package dev.whyoleg.foreign.tooling.cx.bridge.model

import kotlinx.serialization.*
import kotlin.jvm.*

// common, jvm, jvmAndNix, etc
@Serializable
@JvmInline
public value class CxBridgeFragmentId(public val value: String)

@Serializable // TODO: use maps?
public data class CxBridgeFragment(
    val fragmentId: CxBridgeFragmentId,
    val typedefs: Map<CxBridgeDeclarationId, CxBridgeTypedef>,
    val enums: Map<CxBridgeDeclarationId, CxBridgeEnum>,
    val records: Map<CxBridgeDeclarationId, CxBridgeRecord>,
    val functions: Map<CxBridgeDeclarationId, CxBridgeFunction>,
)
