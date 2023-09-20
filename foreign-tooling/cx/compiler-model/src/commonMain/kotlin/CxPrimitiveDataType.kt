package dev.whyoleg.foreign.tooling.cx.compiler.model

import kotlinx.serialization.*

// TODO: name and where to put it? separate module?
@Serializable
public enum class CxPrimitiveDataType {
    Void, Bool,
    Char, SignedChar, UnsignedChar,
    Short, UnsignedShort,
    Int, UnsignedInt,
    Long, UnsignedLong,
    LongLong, UnsignedLongLong,
    Int128, UnsignedInt128,
    Float, Double, LongDouble
}
