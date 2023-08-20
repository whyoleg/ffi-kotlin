package dev.whyoleg.foreign.index.cx

import kotlinx.serialization.*
import kotlin.jvm.*

@Serializable
@JvmInline
public value class CxDeclarationId(public val value: String)

@Serializable
@JvmInline
public value class CxDeclarationName(public val value: String)

//TODO: may be provide just CxHeaderName?
public fun interface DeclarationPredicate<T : CxDeclarationInfo> {
    public fun matches(header: CxHeaderInfo, declaration: T): Boolean
}

//TODO: is it needed?
public fun interface DeclarationSelector<T : CxDeclarationInfo, R> {
    public fun select(header: CxHeaderInfo, declaration: T): R
}

@Serializable
public sealed class CxDeclarationInfo {
    public abstract val id: CxDeclarationId
    public abstract val name: CxDeclarationName?
}

@Serializable
public data class CxTypedefInfo(
    override val id: CxDeclarationId,
    override val name: CxDeclarationName,
    val aliased: CxTypeInfo,
) : CxDeclarationInfo()

@Serializable
public data class CxRecordInfo(
    override val id: CxDeclarationId,
    override val name: CxDeclarationName?,
    val size: Long, //TODO: what is -2?
    val align: Long,
    val isUnion: Boolean,
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
    override val name: CxDeclarationName?,
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
