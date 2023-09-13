package dev.whyoleg.foreign.tooling.cx.compiler.model

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
    Float, Double, LongDouble
}

