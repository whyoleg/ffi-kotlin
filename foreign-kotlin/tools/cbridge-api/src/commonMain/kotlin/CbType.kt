package dev.whyoleg.foreign.tool.cbridge.api

import kotlinx.serialization.*

@Serializable
public enum class CbNumber {
    Byte, UByte,
    Short, UShort,
    Int, UInt,
    Long, ULong,
    PlatformInt, PlatformUInt,
    Float, Double,
}

@Serializable
public sealed class CbType {

    @SerialName("void")
    @Serializable
    public data object Void : CbType()

    @SerialName("boolean")
    @Serializable
    public data object Boolean : CbType()

    @SerialName("number")
    @Serializable
    public data class Number(val value: CbNumber) : CbType()

    @SerialName("pointer")
    @Serializable
    public data class Pointer(val pointed: CbType) : CbType()

    @SerialName("block_pointer")
    @Serializable
    public data class BlockPointer(val pointed: Function) : CbType()

    @SerialName("array")
    @Serializable
    public data class Array(val elementType: CbType, val size: Int?) : CbType()

    @SerialName("vector")
    @Serializable
    public data class Vector(val elementType: CbType, val size: Int) : CbType()

    @SerialName("enum")
    @Serializable
    public data class Enum(
        val packageName: String,
        val name: String,
    ) : CbType()

    // TODO: should we combine typedef/record into `class`? - probably YES
    @SerialName("typedef")
    @Serializable
    public data class Typedef(
        val packageName: String,
        val name: String,
    ) : CbType()

    @SerialName("record")
    @Serializable
    public data class Record(
        val packageName: String,
        val name: String,
    ) : CbType()

    @SerialName("function")
    @Serializable
    public data class Function(val returnType: CbType, val parameters: List<CbType>) : CbType()

    // TODO!!!
    @SerialName("mixed")
    @Serializable
    public data class Mixed(val variants: Map<CbFragmentName, CbType>) : CbType()

    @SerialName("unsupported")
    @Serializable
    public data class Unsupported(val info: String) : CbType()
}
