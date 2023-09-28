package dev.whyoleg.foreign.tooling.cx.compiler.model

import kotlinx.serialization.*
import kotlin.jvm.*

@Serializable
@JvmInline
public value class CxCompilerDeclarationId(public val value: String)

@Serializable
public sealed class CxCompilerFileId {
    @SerialName("main")
    @Serializable
    public data class Main(val name: String) : CxCompilerFileId()

    @SerialName("included")
    @Serializable
    public data class Included(val value: String) : CxCompilerFileId()

    @SerialName("builtin")
    @Serializable
    public data object Builtin : CxCompilerFileId()
}

@Serializable
public sealed class CxCompilerDeclaration {
    public abstract val id: CxCompilerDeclarationId
    public abstract val fileId: CxCompilerFileId
    public abstract val name: String?
}

@Serializable
public data class CxCompilerVariable(
    override val id: CxCompilerDeclarationId,
    override val fileId: CxCompilerFileId,
    override val name: String,
    val type: CxCompilerDataType
) : CxCompilerDeclaration()

@Serializable
public data class CxCompilerEnum(
    override val id: CxCompilerDeclarationId,
    override val fileId: CxCompilerFileId,
    // TODO: TBD global anonymous vs local anonymous?
    override val name: String?, // could have no name - anonymous - just variables
    val constants: List<Constant>,
) : CxCompilerDeclaration() {
    @Serializable
    public data class Constant(
        val name: String,
        val value: Long,
    )
}

@Serializable
public data class CxCompilerRecord(
    override val id: CxCompilerDeclarationId,
    override val fileId: CxCompilerFileId,
    override val name: String?, // could have no name - anonymous - declared directly in another struct
    val isUnion: Boolean,
    val definition: Definition?, // if null, means there is no definition available - opaque
) : CxCompilerDeclaration() {
    @Serializable
    public data class Definition(
        val size: Long,
        val align: Long,
        val fields: List<Field>
    )

    @Serializable
    public data class Field(
        val name: String?,
        val type: CxCompilerDataType,
        val bitWidth: Int?
    )
}

@Serializable
public data class CxCompilerTypedef(
    override val id: CxCompilerDeclarationId,
    override val fileId: CxCompilerFileId,
    override val name: String,
    val aliasedType: CxCompilerDataType,
    val resolvedType: CxCompilerDataType
) : CxCompilerDeclaration()

@Serializable
public data class CxCompilerFunction(
    override val id: CxCompilerDeclarationId,
    override val fileId: CxCompilerFileId,
    override val name: String,
    val isVariadic: Boolean,
    val returnType: CxCompilerDataType,
    val parameters: List<Parameter> = emptyList(),
) : CxCompilerDeclaration() {
    @Serializable
    public data class Parameter(
        val name: String?,
        val type: CxCompilerDataType,
    )
}
