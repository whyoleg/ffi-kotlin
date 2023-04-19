package dev.whyoleg.foreign.cx.index

import kotlinx.serialization.*
import kotlin.jvm.*

@Serializable
@JvmInline
public value class CxDeclarationId(public val value: String)

@Serializable
@JvmInline
public value class CxDeclarationName(public val value: String)

@Serializable
public sealed class CxDeclarationInfo {
    public abstract val id: CxDeclarationId
    public abstract val name: CxDeclarationName
}

@Serializable
public data class CxTypedefInfo(
    override val id: CxDeclarationId,
    override val name: CxDeclarationName,
    val aliased: CxTypeInfo,
) : CxDeclarationInfo()

@Serializable
public data class CxStructInfo(
    override val id: CxDeclarationId,
    override val name: CxDeclarationName,
    val size: Long, //TODO: what is -2?
    val align: Long,
    val fields: List<Field>,
) : CxDeclarationInfo() {
    @Serializable
    public data class Field(
        val name: String,
        val type: CxTypeInfo,
    )
}

@Serializable
public data class CxEnumInfo(
    override val id: CxDeclarationId,
    override val name: CxDeclarationName,
    val constants: List<Constant>,
) : CxDeclarationInfo() {
    @Serializable
    public data class Constant(
        val name: String,
        val value: Long,
    )
}

@Serializable
public data class CxFunctionInfo(
    override val id: CxDeclarationId,
    override val name: CxDeclarationName,
    val returnType: CxTypeInfo,
    val parameters: List<Parameter> = emptyList(),
) : CxDeclarationInfo() {
    @Serializable
    public data class Parameter(
        val name: String,
        val type: CxTypeInfo,
    )
}
