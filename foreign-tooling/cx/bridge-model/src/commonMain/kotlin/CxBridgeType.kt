package dev.whyoleg.foreign.tooling.cx.bridge.model

import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import kotlinx.serialization.*

// TODO: custom serializer?
@Serializable
public sealed class CxBridgeType {
    @Serializable
    public data class Builtin(val type: CxBuiltinType) : CxBridgeType()

    @Serializable
    public data object Void : CxBridgeType()

    @Serializable
    public data object Boolean : CxBridgeType()

    @Serializable
    public data class Pointer(val pointed: CxBridgeType) : CxBridgeType()

    @Serializable
    public data class Enum(val id: CxBridgeDeclarationId) : CxBridgeType()

    @Serializable
    public data class Typedef(val id: CxBridgeDeclarationId) : CxBridgeType()

    @Serializable
    public data class Record(val id: CxBridgeDeclarationId) : CxBridgeType()

    @Serializable
    public data class Function(val returnType: CxBridgeType, val parameters: List<CxBridgeType>) : CxBridgeType()

    @Serializable
    public data class Array(val elementType: CxBridgeType) : CxBridgeType()

    @Serializable // TODO: better name?
    public data class Shared(
        // bridge type here is strictly from fragment
        val variants: Map<CxBridgeFragmentId, CxBridgeType>
    ) : CxBridgeType()
}
