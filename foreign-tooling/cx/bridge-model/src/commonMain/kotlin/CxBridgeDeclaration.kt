package dev.whyoleg.foreign.tooling.cx.bridge.model

import kotlinx.serialization.*

@Serializable
public data class CxBridgeDeclarationId(
    val name: String,
    val packageName: String
)

@Serializable
public sealed class CxBridgeDeclaration {
    public abstract val id: CxBridgeDeclarationId
}

@Serializable
public data class CxBridgeTypedef(
    override val id: CxBridgeDeclarationId,
    val aliased: CxBridgeType
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
        val type: CxBridgeType
    )
}

@Serializable
public data class CxBridgeFunction(
    override val id: CxBridgeDeclarationId,
    val returnType: CxBridgeType,
    val parameters: List<Parameter>
) : CxBridgeDeclaration() {
    @Serializable
    public data class Parameter(
        val name: String,
        val type: CxBridgeType,
    )
}
