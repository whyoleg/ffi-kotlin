package dev.whyoleg.foreign.cx.index

import kotlinx.serialization.*

@Serializable
public enum class CxBuiltinType {
    // Char for string/bytes
    Char, SignedChar, UnsignedChar,
    Short, UnsignedShort,
    Int, UnsignedInt,
    Long, UnsignedLong,
    LongLong, UnsignedLongLong,
    Int128, UnsignedInt128,
    Float, Double, LongDouble,
    Pointer,
}

@Serializable
public data class CxBuiltinTypeSizes(
    val sizes: Map<CxBuiltinType, Long>
)
