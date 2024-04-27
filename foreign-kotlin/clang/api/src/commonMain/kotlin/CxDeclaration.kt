package dev.whyoleg.foreign.clang.api

import kotlinx.serialization.*

public typealias CxDeclarationId = String
public typealias CxDeclarationName = String
public typealias CxDeclarationHeader = String

@Serializable
public data class CxDeclarationDescription(
    val id: CxDeclarationId,
    val name: CxDeclarationName,
    val header: CxDeclarationHeader // "" for builtins
)

@Serializable
public sealed class CxDeclaration {
    public abstract val description: CxDeclarationDescription
}

// TODO: add `isConst`
@SerialName("variable")
@Serializable
public data class CxVariable(
    override val description: CxDeclarationDescription,
    val variableType: CxType
) : CxDeclaration()

// TODO: decide on unnamed enum!!!
// enum could be unnamed (description.name="")
// enum without a name - just a bag of constants
@SerialName("enum")
@Serializable
public data class CxEnum(
    override val description: CxDeclarationDescription,
    val constants: List<CxEnumConstant>
) : CxDeclaration()

@Serializable
public data class CxEnumConstant(
    val name: String,
    val value: Long
)

// TODO: Decide on resolved type
//  may be just replace with separate type
//  something like kind: Number(width), Void, Record, Pointer, etc
//  TBD how it will be used
//  could be useful for numbers, where it's typedef of typedef of typedef
@SerialName("typedef")
@Serializable
public data class CxTypedef(
    override val description: CxDeclarationDescription,
    val aliasedType: CxType,
    val resolvedType: CxType
) : CxDeclaration()

// WTF: https://stackoverflow.com/questions/38457109/c-how-to-access-different-types-of-anonymous-or-unnamed-nested-structs
@SerialName("record")
@Serializable
public data class CxRecord(
    override val description: CxDeclarationDescription,
    val definition: CxRecordDefinition? // if null -> opaque
) : CxDeclaration()

@Serializable
public data class CxRecordDefinition(
    val isUnion: Boolean,
    // TODO: size and align may be not needed
    val size: Long,
    val align: Long,
    val fields: List<CxRecordField>,
    val anonymousRecords: Map<CxDeclarationId, CxRecordDefinition>
)

// TODO: field could have no name if it's a bit field
//  decide how to work with bitfields
// if name=null for record -> record should be inlined
@Serializable
public data class CxRecordField(
    val name: String?,
    val fieldType: CxType,
    val bitWidth: Int?
)

@SerialName("function")
@Serializable
public data class CxFunction(
    override val description: CxDeclarationDescription,
    val isVariadic: Boolean,
    val returnType: CxType,
    val parameters: List<CxFunctionParameter>,
) : CxDeclaration()

@Serializable
public data class CxFunctionParameter(
    val name: String?,
    val type: CxType
)
