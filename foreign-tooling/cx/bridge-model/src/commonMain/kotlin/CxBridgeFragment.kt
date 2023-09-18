package dev.whyoleg.foreign.tooling.cx.bridge.model

import kotlinx.serialization.*

// name - common, jvm, jvmAndNix, etc
@Serializable
public data class CxBridgeFragment(
    val name: String,
    val typedefs: List<CxBridgeTypedef>,
    val enums: List<CxBridgeEnum>,
    val records: List<CxBridgeRecord>,
    val functions: List<CxBridgeFunction>
)
