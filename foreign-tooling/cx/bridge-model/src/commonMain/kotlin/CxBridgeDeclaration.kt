package dev.whyoleg.foreign.tooling.cx.bridge.model

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable(with = CxBridgeDeclarationIdSerializer::class)
public data class CxBridgeDeclarationId(
    val packageName: String,
    val declarationName: String
)

private object CxBridgeDeclarationIdSerializer : KSerializer<CxBridgeDeclarationId> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("CxBridgeDeclarationId", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): CxBridgeDeclarationId {
        val list = decoder.decodeString().split(":")
        check(list.size == 2)
        val (packageName, declarationName) = list
        return CxBridgeDeclarationId(packageName, declarationName)
    }

    override fun serialize(encoder: Encoder, value: CxBridgeDeclarationId) {
        encoder.encodeString("${value.packageName}:${value.declarationName}")
    }
}

@Serializable
public sealed class CxBridgeDeclaration {
    public abstract val id: CxBridgeDeclarationId
}

@Serializable
public data class CxBridgeTypedef(
    override val id: CxBridgeDeclarationId,
    val aliased: CxBridgeDataType
) : CxBridgeDeclaration()

@Serializable
public data class CxBridgeEnum(
    override val id: CxBridgeDeclarationId,
    val constantNames: Set<String>,
) : CxBridgeDeclaration()

@Serializable
public data class CxBridgeRecord(
    override val id: CxBridgeDeclarationId,
    val isUnion: Boolean,
    val fields: List<Field>? // if null - opaque
) : CxBridgeDeclaration() {
    @Serializable
    public data class Field(
        val name: String,
        val type: CxBridgeDataType
    )
}

@Serializable
public data class CxBridgeFunction(
    override val id: CxBridgeDeclarationId,
    val returnType: CxBridgeDataType,
    val parameters: List<Parameter>
) : CxBridgeDeclaration() {
    @Serializable
    public data class Parameter(
        val name: String,
        val type: CxBridgeDataType,
    )
}
