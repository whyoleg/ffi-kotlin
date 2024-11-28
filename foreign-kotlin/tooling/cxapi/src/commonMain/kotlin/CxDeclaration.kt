package dev.whyoleg.foreign.tooling.cxapi

import kotlinx.serialization.*

public typealias CxDeclarationId = String
public typealias CxDeclarationName = String
public typealias CxDeclarationHeader = String

@Serializable
public data class CxDeclarationDescription(
    val id: CxDeclarationId,
    val name: CxDeclarationName,
    val header: CxDeclarationHeader // TODO: empty string for builtins
)

public sealed class CxDeclaration {
    public abstract val description: CxDeclarationDescription
}

@Serializable
public data class CxVariable(
    override val description: CxDeclarationDescription,
    val isConst: Boolean,
    val type: CxType,
) : CxDeclaration()

@Serializable
public data class CxEnum(
    override val description: CxDeclarationDescription,
    val constants: List<CxEnumConstant>
) : CxDeclaration()

@Serializable
public data class CxUnnamedEnumConstant(
    override val description: CxDeclarationDescription,
    val value: Long,
    val enumId: CxDeclarationId // just for grouping
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

// https://stackoverflow.com/questions/38457109/c-how-to-access-different-types-of-anonymous-or-unnamed-nested-structs
@Serializable
public data class CxRecord(
    override val description: CxDeclarationDescription,
    val definition: CxRecordDefinition? // if null -> opaque
) : CxDeclaration()

@Serializable
public data class CxRecordDefinition(
    val isUnion: Boolean,
    val byteSize: Long,
    val byteAlignment: Long,
    val fields: List<CxRecordField>,
    val anonymousRecords: Map<CxDeclarationId, CxRecordDefinition>
)

// TODO: field could have no name if it's a bit field
//  decide how to work with bitfields
//  if name=null for record -> record should be inlined
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
