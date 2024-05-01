package dev.whyoleg.foreign.bridge.c

import kotlinx.serialization.*

// packageName/name
// foreign.libcrypto/OSSL_PARAM
// for anonymous
// foreign.libcrypto/OSSL_PARAM|1
// foreign.libcrypto/OSSL_PARAM|2
// foreign.libcrypto/OSSL_PARAM|3
public typealias CDeclarationId = String

@Serializable
public data class CDeclarationDescription(
    val id: CDeclarationId,
    val packageName: String,
    val headerName: String,
    val ktName: String, // could be empty for unnamed enum - TODO how to handle it ID?
    val cName: String, // could be empty for unnamed enum
    val availableOn: List<String>?, // if `null` - available on all targets
)

public sealed class CDeclaration {
    public abstract val description: CDeclarationDescription
}

// TODO: refactor to support expect/actual

@Serializable
public data class CVariable(
    override val description: CDeclarationDescription,
    val type: CType
) : CDeclaration()

@Serializable
public data class CEnum(
    override val description: CDeclarationDescription,
    val constants: List<CEnumConstant>
) : CDeclaration()

@Serializable
public data class CEnumConstant(
    val ktName: String,
    val cName: String,
    val value: Long
)

@Serializable
public data class CTypedef(
    override val description: CDeclarationDescription,
    val aliasedType: CType,
    val resolvedType: CType
) : CDeclaration()

@Serializable
public data class CRecord(
    override val description: CDeclarationDescription,
    val definition: CRecordDefinition?
) : CDeclaration()

@Serializable
public data class CRecordDefinition(
    val isUnion: Boolean,
    val size: Long, // in bytes
    val align: Long, // in bytes
    val anonymousRecords: Map<CDeclarationId, CRecordDefinition>, // TODO
    val fields: List<CRecordField>
)

// no bit fields support in bridge
@Serializable
public data class CRecordField(
    val ktName: String,
    val cName: String,
    val type: CType,
    val offset: Long // in bytes
)

@Serializable
public data class CFunction(
    override val description: CDeclarationDescription,
    val isVariadic: Boolean,
    val returnType: CType,
    val parameters: List<CFunctionParameter>
) : CDeclaration()

@Serializable
public data class CFunctionParameter(
    val ktName: String,
    val cNames: List<String>, // multiple names because of commonization
    val type: CType,
)
