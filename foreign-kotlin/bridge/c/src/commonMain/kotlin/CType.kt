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
    Mixed
}

@Serializable
public sealed class CType {

    @Serializable
    public data object Void : CType()

    @Serializable
    public data object Boolean : CType()

    @Serializable
    public data class Number(val value: CNumber) : CType()

    @Serializable
    public data class Pointer(val pointed: CType) : CType()

    @Serializable
    public data class Enum(val id: CDeclarationId) : CType()

    @Serializable
    public data class Typedef(val id: CDeclarationId) : CType()

    @Serializable
    public data class Record(val id: CDeclarationId) : CType()

    @Serializable
    public data class Function(val returnType: CType, val parameters: List<CType>) : CType()

    @Serializable
    public data class Array(val elementType: CType, val size: Int?) : CType()

    @Serializable
    public data class Mixed(val variants: List<CType>) : CType()

    @Serializable
    public data class Unsupported(val info: String) : CType()
}
