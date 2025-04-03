package dev.whyoleg.foreign.tool.cbridge.api

import kotlinx.serialization.*

public typealias CbTarget = String
public typealias CbFragmentName = String

// properties = variables + unnamed enums
// functions = functions
// enums = named enums
// classes = records + opaque + typedefs

@Serializable
public data class CbFragment(
    public val name: CbFragmentName,
    public val target: CbTarget?, // null -> shared fragment
    public val packages: List<CbPackage>,
)

@Serializable
public data class CbPackage(
    val name: String,
    val enums: List<CbEnum>,
    val properties: List<CbProperty>,
    val functions: List<CbFunction>,
    val classes: List<CbClass>
)

//@JvmInline
//public value class SomeThreadType(
//    public val value: Long
//) {
//    override fun toString(): String = when (value) {
//        0L   -> "A"
//        1L   -> "B"
//        else -> "SomeEnum($value)"
//    }
//
//    public companion object {
//        public inline val A: SomeThreadType get() = SomeThreadType(0)
//        public inline val B: SomeThreadType get() = SomeThreadType(1)
//    }
//
//    public enum class Exhaustive(public val value: Long) {
//        A(0),
//        B(1)
//    }
//}

@Serializable
public data class CbEnum(
    val name: String,
    val constants: List<CbEnumConstant>,
)

@Serializable
public data class CbEnumConstant(
    val name: String,
    val value: Long? // null for partial
)

//public val s: String
//    get() = TODO()
//
@Serializable
public data class CbProperty(
    val name: String,
    val isMutable: Boolean, // TODO
    val type: CbType,
)

@Serializable
public data class CbFunction(
    val name: String,
    val isVariadic: Boolean,
    val returnType: CbType,
    val parameters: List<CbFunctionParameter>
)

@Serializable
public data class CbFunctionParameter(
    val name: String,
    val alternativeNames: List<String>, // when combining
    val type: CbType,
)

@Serializable
public sealed class CbClass {
    public abstract val name: String
}

@Serializable
public data class CbTypedef(
    override val name: String,
    val aliasedType: CbType,
    val resolvedType: CbType
) : CbClass()

@Serializable
public data class CbOpaque(
    override val name: String
) : CbClass()

@Serializable
public data class CbRecord(
    override val name: String,
    val isUnion: Boolean,
    val fields: List<CRecordField>,
    val anonymousRecords: List<CbRecord>
) : CbClass()

@Serializable
public data class CRecordField(
    val name: String,
    val type: CbType
)
