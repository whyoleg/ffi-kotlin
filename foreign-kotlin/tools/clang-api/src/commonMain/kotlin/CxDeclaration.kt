package dev.whyoleg.foreign.tool.clang.api

import kotlinx.serialization.*

public typealias CxDeclarationId = String

@Serializable
public data class CxDeclaration(
    val id: CxDeclarationId,
    val name: String?,
    val isAnonymous: Boolean,
    val header: String?,
    val data: CxDeclarationData
)

@Serializable
public sealed class CxDeclarationData

@SerialName("variable")
@Serializable
public data class CxVariableData(
    val isConst: Boolean,
    val type: CxType,
) : CxDeclarationData()

@SerialName("enum")
@Serializable
public data class CxEnumData(
    val constants: List<CxEnumConstant>
) : CxDeclarationData()

@Serializable
public data class CxEnumConstant(
    val name: String,
    val value: Long
)

@SerialName("typedef")
@Serializable
public data class CxTypedefData(
    val aliasedType: CxType,
    val resolvedType: CxType
) : CxDeclarationData()

// https://stackoverflow.com/questions/38457109/c-how-to-access-different-types-of-anonymous-or-unnamed-nested-structs
@SerialName("opaque")
@Serializable
public data object CxOpaqueData : CxDeclarationData()

@SerialName("record")
@Serializable
public data class CxRecordData(
    val isUnion: Boolean,
    val byteSize: Long,
    val byteAlignment: Long,
    val fields: List<CxRecordField>,
    val anonymousRecords: Set<CxDeclarationId>
) : CxDeclarationData()

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

@SerialName("function")
@Serializable
public data class CxFunctionData(
    val isVariadic: Boolean,
    val returnType: CxType,
    val parameters: List<CxFunctionParameter>,
) : CxDeclarationData()

@Serializable
public data class CxFunctionParameter(
    val name: String?,
    val type: CxType
)
