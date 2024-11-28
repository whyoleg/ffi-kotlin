package dev.whyoleg.foreign.tooling.cbridge

import kotlinx.serialization.*

// packageName/name
// foreign.libcrypto/OSSL_PARAM
// for anonymous
// foreign.libcrypto/OSSL_PARAM|1
// foreign.libcrypto/OSSL_PARAM|2
// foreign.libcrypto/OSSL_PARAM|3
public typealias CDeclarationId = String
public typealias CDeclarationName = String // kotlin name
public typealias CDeclarationPackageName = String // kotlin name

@Serializable
public data class CDeclarationDescription(
    val id: CDeclarationId,
    val name: CDeclarationName,
    val packageName: CDeclarationPackageName,
)

public sealed class CDeclaration {
    public abstract val description: CDeclarationDescription
}

@Serializable
public data class CVariable(
    override val description: CDeclarationDescription,
    val type: CType
) : CDeclaration()

// TODO: how to commonize enums?
@Serializable
public data class CEnum(
    override val description: CDeclarationDescription,
    val constants: List<CEnumConstant>
) : CDeclaration()

@Serializable
public data class CUnnamedEnumConstant(
    override val description: CDeclarationDescription,
    val enumId: CDeclarationId // just for grouping
) : CDeclaration()

@Serializable
public data class CEnumConstant(
    val name: String,
    // val value: Long? // null for partial
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
    val definition: CRecordDefinition? // if null -> opaque
) : CDeclaration()

@Serializable
public data class CRecordDefinition(
    val isUnion: Boolean,
    val fields: List<CRecordField>,
    val anonymousRecords: Map<CDeclarationId, CRecordDefinition>
)

@Serializable
public data class CRecordField(
    val name: String,
    val type: CType
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
    val name: String,
    val type: CType,
)
