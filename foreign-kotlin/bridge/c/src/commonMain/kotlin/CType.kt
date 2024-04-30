package dev.whyoleg.foreign.bridge.c

import kotlinx.serialization.*

@Serializable
public enum class CNumber {
    Byte, UByte,
    Short, UShort,
    Int, UInt,
    Long, ULong,
    PlatformInt, PlatformUInt,
    Float, Double,
}

@Serializable
public sealed class CType {

    @SerialName("void")
    @Serializable
    public data object Void : CType()

    @SerialName("boolean")
    @Serializable
    public data object Boolean : CType()

    @SerialName("number")
    @Serializable
    public data class Number(val value: CNumber) : CType()

    @SerialName("pointer")
    @Serializable
    public data class Pointer(val pointed: CType) : CType()

    @SerialName("enum")
    @Serializable
    public data class Enum(val id: CDeclarationId) : CType()

    @SerialName("typedef")
    @Serializable
    public data class Typedef(val id: CDeclarationId) : CType()

    @SerialName("record")
    @Serializable
    public data class Record(val id: CDeclarationId) : CType()

    @SerialName("function")
    @Serializable
    public data class Function(val returnType: CType, val parameters: List<CType>) : CType()

    @SerialName("array")
    @Serializable
    public data class Array(val elementType: CType, val size: Int?) : CType()

    @SerialName("mixed")
    @Serializable
    public data class Mixed(val variants: List<CType>) : CType()

    @SerialName("unsupported")
    @Serializable
    public data class Unsupported(val info: String) : CType()
}
