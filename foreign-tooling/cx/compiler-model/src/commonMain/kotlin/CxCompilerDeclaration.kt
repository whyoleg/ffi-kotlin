package dev.whyoleg.foreign.tooling.cx.compiler.model

import kotlinx.serialization.*
import kotlin.jvm.*

@Serializable
@JvmInline
public value class CxCompilerDeclarationId(public val value: String)

@Serializable
public sealed class CxCompilerDeclaration {
    public abstract val id: CxCompilerDeclarationId
    public abstract val declarationName: String?
    public abstract val headerName: String?
}

@Serializable
public data class CxCompilerTypedef(
    override val id: CxCompilerDeclarationId,
    override val declarationName: String,
    override val headerName: String?,
    val aliased: CxCompilerDataType,
) : CxCompilerDeclaration()

@Serializable
public data class CxCompilerRecord(
    override val id: CxCompilerDeclarationId,
    override val declarationName: String?, // if null - anonymous
    override val headerName: String?,
    val isUnion: Boolean,
    val members: Members? // if null - opaque
) : CxCompilerDeclaration() {
    @Serializable
    public data class Field(
        val name: String,
        val type: CxCompilerDataType,
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
public data class CxCompilerEnum(
    override val id: CxCompilerDeclarationId,
    override val declarationName: String?, // if null - anonymous
    override val headerName: String?,
    val constants: List<Constant>,
) : CxCompilerDeclaration() {
    @Serializable
    public data class Constant(
        val name: String,
        val value: Long,
    )
}

@Serializable
public data class CxCompilerFunction(
    override val id: CxCompilerDeclarationId,
    override val declarationName: String,
    override val headerName: String?,
    val returnType: CxCompilerDataType,
    val parameters: List<Parameter> = emptyList(),
) : CxCompilerDeclaration() {

    // TODO: add if const?
    @Serializable
    public data class Parameter(
        val name: String,
        val type: CxCompilerDataType,
    )
}
