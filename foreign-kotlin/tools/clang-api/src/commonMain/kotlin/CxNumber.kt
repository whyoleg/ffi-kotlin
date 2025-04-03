package dev.whyoleg.foreign.tool.clang.api

import kotlinx.serialization.*

@Serializable
public enum class CxNumber {
    Char, SignedChar, UnsignedChar,
    Short, UnsignedShort,
    Int, UnsignedInt,
    Long, UnsignedLong,
    LongLong, UnsignedLongLong,
    Int128, UnsignedInt128,
    Float, Float16, Double, LongDouble
}

@Serializable
public enum class CxNumberWidth {
    Int8, // byte/char
    Int16, // short
    Int32, // int / long
    Int64, // long long
    Int128, // int128
}

// TODO: change map value?
public class CxNumbers private constructor(
    public val values: Map<CxNumber, Int>
) {
    public fun sizeOf(type: CxNumber): Int = values.getValue(type)
    public fun width(type: CxNumber): CxNumberWidth = when (sizeOf(type)) {
        Byte.SIZE_BYTES     -> CxNumberWidth.Int8
        Short.SIZE_BYTES    -> CxNumberWidth.Int16
        Int.SIZE_BYTES      -> CxNumberWidth.Int32
        Long.SIZE_BYTES     -> CxNumberWidth.Int64
        Long.SIZE_BYTES * 2 -> CxNumberWidth.Int128
        else                -> error("should not happen")
    }

    public companion object {
        // TODO: support all K targets
        public val MacosArm64: CxNumbers = primitiveDataTypes(
            longSize = Long.SIZE_BYTES,
            longDoubleSize = Long.SIZE_BYTES
        )
        public val MacosX64: CxNumbers = primitiveDataTypes(
            longSize = Long.SIZE_BYTES,
            longDoubleSize = Long.SIZE_BYTES * 2
        )
        public val LinuxX64: CxNumbers = primitiveDataTypes(
            longSize = Long.SIZE_BYTES,
            longDoubleSize = Long.SIZE_BYTES * 2
        )
        public val LinuxArm64: CxNumbers = primitiveDataTypes(
            longSize = Long.SIZE_BYTES,
            longDoubleSize = Long.SIZE_BYTES * 2
        )
        public val MingwX64: CxNumbers = primitiveDataTypes(
            longSize = Int.SIZE_BYTES,
            longDoubleSize = Long.SIZE_BYTES * 2
        )
        public val IosDeviceArm64: CxNumbers = primitiveDataTypes(
            longSize = Long.SIZE_BYTES,
            longDoubleSize = Long.SIZE_BYTES
        )
        public val IosSimulatorArm64: CxNumbers = primitiveDataTypes(
            longSize = Long.SIZE_BYTES,
            longDoubleSize = Long.SIZE_BYTES
        )
        public val IosSimulatorX64: CxNumbers = primitiveDataTypes(
            longSize = Long.SIZE_BYTES,
            longDoubleSize = Long.SIZE_BYTES * 2
        )

        private fun primitiveDataTypes(
            longSize: Int,
            longDoubleSize: Int
        ): CxNumbers = CxNumbers(
            mapOf(
                CxNumber.Char to Byte.SIZE_BYTES,
                CxNumber.UnsignedChar to Byte.SIZE_BYTES,
                CxNumber.SignedChar to Byte.SIZE_BYTES,
                CxNumber.Short to Short.SIZE_BYTES,
                CxNumber.UnsignedShort to Short.SIZE_BYTES,
                CxNumber.Int to Int.SIZE_BYTES,
                CxNumber.UnsignedInt to Int.SIZE_BYTES,
                CxNumber.Long to longSize,
                CxNumber.UnsignedLong to longSize,
                CxNumber.LongLong to Long.SIZE_BYTES,
                CxNumber.UnsignedLongLong to Long.SIZE_BYTES,
                CxNumber.Float to Int.SIZE_BYTES,
                //CxNumber.Float16 to Short.SIZE_BYTES, // TODO
                CxNumber.Double to Long.SIZE_BYTES,
                CxNumber.LongDouble to longDoubleSize,
                CxNumber.Int128 to Long.SIZE_BYTES * 2,
                CxNumber.UnsignedInt128 to Long.SIZE_BYTES * 2
            )
        )
    }
}
