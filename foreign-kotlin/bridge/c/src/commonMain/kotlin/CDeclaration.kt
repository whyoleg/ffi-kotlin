package dev.whyoleg.foreign.bridge.c

import kotlinx.serialization.*

public typealias CDeclarations<D> = List<CDeclaration<D>>
public typealias CName = String

@Serializable
public data class CDeclarationId(
    val packageName: String,
    val name: String,
    val parentName: String? = null
) {
    init {
        check(packageName.isNotBlank()) { "package name may not be blank" }
        check(name.isNotBlank()) { "name may not be blank" }
        check(parentName == null || parentName.isNotBlank()) { "parent name may not be blank" }
    }

    public constructor(packageName: String, name: String, parentId: CDeclarationId?) : this(
        packageName = packageName,
        name = name,
        parentName = parentId?.name
    ) {
        check(parentId == null || parentId.packageName == packageName) { "parent.packageName should be the same" }
    }
}

@Serializable
public class CDeclaration<D : CDeclarationData>(
    public val id: CDeclarationId,
    public val fileName: String, // kotlin file name, coresponds to header
    public val availableOn: List<String>?, // if `null` - available on all targets
    public val data: D
)

@Serializable
public sealed class CDeclarationData

@SerialName("variable")
@Serializable
public data class CVariableData(val cName: CName, val type: CType) : CDeclarationData()

@SerialName("enum")
@Serializable
public data class CEnumData(val cName: CName?, val constants: Set<CEnumConstant>) : CDeclarationData()

@Serializable
public data class CEnumConstant(val cName: CName, val name: String, val value: Long)

@SerialName("typedef")
@Serializable
public data class CTypedefData(val cName: CName, val aliasedType: CType, val resolvedType: CType) : CDeclarationData()

@Serializable
public sealed class CRecordData : CDeclarationData()

@SerialName("record.opaque")
@Serializable
public data class COpaqueRecordData(val cName: CName) : CRecordData()

// basic simple record
@SerialName("record.basic")
@Serializable
public data class CBasicRecordData(val cName: CName, val definition: CRecordDefinition) : CRecordData()

// used in struct fields;
// could be declared in an array/pointer
@SerialName("record.anonymous")
@Serializable
public data class CAnonymousRecordData(val definition: CRecordDefinition) : CRecordData()

@Serializable
public data class CRecordDefinition(
    val isUnion: Boolean,
    // TODO: size and align may be not needed?
    val size: Long,
    val align: Long,
    val fields: List<CRecordField>,
)

// TODO: field could have no name if it's a bit field
@Serializable
public data class CRecordField(
    val cName: CName?, // TODO: should we have multiple names here?
    val name: String?,
    val fieldType: CType,
    val bitWidth: Int?
)

@SerialName("function")
@Serializable
public data class CFunctionData(
    val cName: CName,
    val isVariadic: Boolean,
    val returnType: CType,
    val parameters: List<CFunctionParameter>
) : CDeclarationData()

@Serializable
public data class CFunctionParameter(
    // multiple names because of commonization
    val cNames: List<CName>,
    val name: String,
    val type: CType,
)
