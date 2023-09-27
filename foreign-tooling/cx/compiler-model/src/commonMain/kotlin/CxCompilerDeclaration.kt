package dev.whyoleg.foreign.tooling.cx.compiler.model

import kotlinx.serialization.*
import kotlin.jvm.*

public typealias CxCompilerDeclarations<T> = Map<CxCompilerDeclarationId, CxCompilerDeclaration<T>>

@Serializable
@JvmInline
public value class CxCompilerDeclarationId(public val value: String)

@Serializable
public sealed class CxCompilerHeaderId {
    @Serializable
    public data class Main(val name: String) : CxCompilerHeaderId()

    @Serializable
    public data class Included(val value: String) : CxCompilerHeaderId()

    @Serializable
    public data object Builtin : CxCompilerHeaderId()
}

@Serializable
public sealed class CxCompilerDeclarationData

@Serializable
public data class CxCompilerDeclaration<T : CxCompilerDeclarationData?>(
    val id: CxCompilerDeclarationId,
    val declarationName: String,
    val headerId: CxCompilerHeaderId,
    val data: T
)

@Serializable
public data class CxCompilerVariableData(
    val returnType: CxCompilerDataType
) : CxCompilerDeclarationData()

@Serializable
public data class CxCompilerTypedefData(
    val aliasedType: CxCompilerDataType,
    val resolvedType: CxCompilerDataType
) : CxCompilerDeclarationData()

@Serializable
public data class CxCompilerRecordData(
    val isUnion: Boolean,
    val size: Long,
    val align: Long,
    val fields: List<Field>
) : CxCompilerDeclarationData() {
    @Serializable
    public data class Field(
        val name: String?,
        val type: CxCompilerDataType,
        val bitWidth: Int?
    )
}

@Serializable
public data class CxCompilerEnumData(
    val unnamed: Boolean, // TODO: need to drop it
    val constants: List<Constant>,
) : CxCompilerDeclarationData() {
    @Serializable
    public data class Constant(
        val name: String,
        val value: Long,
    )
}

@Serializable
public data class CxCompilerFunctionData(
    val isVariadic: Boolean,
    val returnType: CxCompilerDataType,
    val parameters: List<Parameter> = emptyList(),
) : CxCompilerDeclarationData() {
    @Serializable
    public data class Parameter(
        val name: String?,
        val type: CxCompilerDataType,
    )
}
