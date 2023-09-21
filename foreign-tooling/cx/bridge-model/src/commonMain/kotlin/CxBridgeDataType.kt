package dev.whyoleg.foreign.tooling.cx.bridge.model

import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import kotlinx.serialization.*

@Serializable // TODO: custom serializer?
public sealed class CxBridgeDataType {
    @Serializable
    public data class Primitive(val value: CxPrimitiveDataType) : CxBridgeDataType()

    @Serializable
    public data class Pointer(val pointed: CxBridgeDataType) : CxBridgeDataType()

    @Serializable
    public data class Enum(val id: CxBridgeDeclarationId) : CxBridgeDataType()

    @Serializable
    public data class Typedef(val id: CxBridgeDeclarationId) : CxBridgeDataType()

    @Serializable
    public data class Record(val id: CxBridgeDeclarationId) : CxBridgeDataType()

    @Serializable
    public data class Function(val returnType: CxBridgeDataType, val parameters: List<CxBridgeDataType>) : CxBridgeDataType()

    @Serializable
    public data class Array(val elementType: CxBridgeDataType) : CxBridgeDataType()

    @Serializable
    public data class Unsupported(val name: String, val kind: String) : CxBridgeDataType()

    @Serializable // TODO: better name?
    public data class Shared(
        // bridge type here is strictly from fragment
        val variants: Map<CxBridgeFragmentId, CxBridgeDataType>
    ) : CxBridgeDataType()
}
