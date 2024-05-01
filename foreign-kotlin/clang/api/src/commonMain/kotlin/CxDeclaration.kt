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

public sealed class CxDeclaration {
    public abstract val description: CxDeclarationDescription
}

@Serializable
public data class CxVariable(
    override val description: CxDeclarationDescription,
    val isConst: Boolean, // TODO?
    val type: CxType
) : CxDeclaration()

// TODO: decide on unnamed enum!!!
// enum could be unnamed (description.name="")
// enum without a name - just a bag of constants
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

@Serializable
public data class CxTypedef(
    override val description: CxDeclarationDescription,
    val aliasedType: CxType,
    val resolvedType: CxType
) : CxDeclaration()

// WTF: https://stackoverflow.com/questions/38457109/c-how-to-access-different-types-of-anonymous-or-unnamed-nested-structs
@Serializable
public data class CxRecord(
    override val description: CxDeclarationDescription,
    val definition: CxRecordDefinition? // if null -> opaque
) : CxDeclaration()

@Serializable
public data class CxRecordDefinition(
    val isUnion: Boolean,
    val size: Long, // in bytes
    val align: Long, // in bytes
    val fields: List<CxRecordField>,
    val anonymousRecords: Map<CxDeclarationId, CxRecordDefinition>
)

// TODO: field could have no name if it's a bit field
//  decide how to work with bitfields
// if name=null for record -> record should be inlined
@Serializable
public data class CxRecordField(
    val name: String?,
    val type: CxType,
    val bitOffset: Long,
    val bitWidth: Int?,
)

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
