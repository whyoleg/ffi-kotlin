package dev.whyoleg.foreign.clang.api

import kotlinx.serialization.*

public typealias CxDeclarationId = String
public typealias CxDeclarationHeader = String
public typealias CxDeclarations<D> = List<CxDeclaration<D>>

@Serializable
public data class CxDeclaration<D : CxDeclarationData>(
    public val id: CxDeclarationId,
    public val header: CxDeclarationHeader,
    public val data: D
)

@Serializable
public sealed class CxDeclarationData

// TODO: add `isConst`
@SerialName("variable")
@Serializable
public data class CxVariableData(val name: String, val variableType: CxType) : CxDeclarationData()

// enum without a name - just a bag of constants
@SerialName("enum")
@Serializable
public data class CxEnumData(val name: String?, val constants: List<CxEnumConstant>) : CxDeclarationData()

@Serializable
public data class CxEnumConstant(val name: String, val value: Long)

// TODO: Decide on resolved type
//  may be just replace with separate type
//  something like kind: Number(width), Void, Record, Pointer, etc
//  TBD how it will be used
//  could be useful for numbers, where it's typedef of typedef of typedef
@SerialName("typedef")
@Serializable
public data class CxTypedefData(
    val name: String,
    val aliasedType: CxType,
    val resolvedType: CxType
) : CxDeclarationData()

// WTF: https://stackoverflow.com/questions/38457109/c-how-to-access-different-types-of-anonymous-or-unnamed-nested-structs
@Serializable
public sealed class CxRecordData : CxDeclarationData()

@SerialName("record.opaque")
@Serializable
public data class CxOpaqueRecordData(val name: String) : CxRecordData()

// basic simple record
@SerialName("record.basic")
@Serializable
public data class CxBasicRecordData(val name: String, val definition: CxRecordDefinition) : CxRecordData()

// used in struct fields;
// could be declared in an array/pointer
@SerialName("record.anonymous")
@Serializable
public data class CxAnonymousRecordData(val definition: CxRecordDefinition) : CxRecordData()

@Serializable
public data class CxRecordDefinition(
    val isUnion: Boolean,
    // TODO: size and align may be not needed
    val size: Long,
    val align: Long,
    val fields: List<CxRecordField>,
)

// TODO: field could have no name if it's a bit field
@Serializable
public data class CxRecordField(val name: String?, val fieldType: CxType, val bitWidth: Int?)

@SerialName("function")
@Serializable
public data class CxFunctionData(
    val name: String,
    val isVariadic: Boolean,
    val returnType: CxType,
    val parameters: List<CxFunctionParameter>,
) : CxDeclarationData()

@Serializable
public data class CxFunctionParameter(val name: String?, val type: CxType)
