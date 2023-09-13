package dev.whyoleg.foreign.tooling.cx.compiler.model

import kotlinx.serialization.*
import kotlin.jvm.*

@Serializable
@JvmInline
public value class CxDeclarationId(public val value: String)

@Serializable
@JvmInline
public value class CxDeclarationName(public val value: String)

@Serializable
@JvmInline
public value class CxHeaderName(public val value: String)

@Serializable
public sealed class CxDeclaration {
    public abstract val id: CxDeclarationId
    public abstract val name: CxDeclarationName?
    public abstract val headerName: CxHeaderName?
}

@Serializable
public data class CxTypedef(
    override val id: CxDeclarationId,
    override val name: CxDeclarationName,
    override val headerName: CxHeaderName?,
    val aliased: CxType,
) : CxDeclaration()

@Serializable
public data class CxRecord(
    override val id: CxDeclarationId,
    override val name: CxDeclarationName?, // if null - anonymous
    override val headerName: CxHeaderName?,
    val isUnion: Boolean,
    val members: Members? // if null - opaque
) : CxDeclaration() {
    @Serializable
    public data class Field(
        val name: String,
        val type: CxType,
    )

    // TODO: better name?
    @Serializable
    public data class Members(
        val size: Long,
        val align: Long,
        val fields: List<Field>,
    )
}

@Serializable
public data class CxEnum(
    override val id: CxDeclarationId,
    override val name: CxDeclarationName?, // if null - anonymous
    override val headerName: CxHeaderName?,
    val constants: List<Constant>,
) : CxDeclaration() {
    @Serializable
    public data class Constant(
        val name: String,
        val value: Long,
    )
}

@Serializable
public data class CxFunction(
    override val id: CxDeclarationId,
    override val name: CxDeclarationName,
    override val headerName: CxHeaderName?,
    val returnType: CxType,
    val parameters: List<Parameter> = emptyList(),
) : CxDeclaration() {

    // TODO: add if const?
    @Serializable
    public data class Parameter(
        val name: String,
        val type: CxType,
    )
}
