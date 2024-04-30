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
    val ktName: String, // could be empty for unnamed enum - how to handle it ID?
    val cName: String, // could be empty for unnamed enum
    val availableOn: List<String>?, // if `null` - available on all targets
)

@Serializable
public sealed class CDeclaration {
    public abstract val description: CDeclarationDescription
}

@SerialName("variable")
@Serializable
public data class CVariable(
    override val description: CDeclarationDescription,
    val variableType: CType
) : CDeclaration()

@SerialName("enum")
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

@SerialName("typedef")
@Serializable
public data class CTypedef(
    override val description: CDeclarationDescription,
    val aliasedType: CType,
    val resolvedType: CType
) : CDeclaration()

@SerialName("record")
@Serializable
public data class CRecord(
    override val description: CDeclarationDescription,
    val definition: CRecordDefinition?
) : CDeclaration()

@Serializable
public data class CRecordDefinition(
    val isUnion: Boolean,
    // TODO: size and align may be not needed?
    val size: Long,
    val align: Long,
    val anonymousRecords: Map<CDeclarationId, CRecordDefinition>, // TODO
    val fields: List<CRecordField>
)

// TODO: field could have no name if it's a bit field
@Serializable
public data class CRecordField(
    val ktName: String,
    val cName: String?, // TODO: should we have multiple names here?
    val fieldType: CType,
    val bitWidth: Int?
)

@SerialName("function")
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
