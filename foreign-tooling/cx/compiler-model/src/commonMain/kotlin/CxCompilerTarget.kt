package dev.whyoleg.foreign.tooling.cx.compiler.model

import kotlinx.serialization.*

@Serializable
public sealed interface CxCompilerTarget {
    public sealed interface Desktop : CxCompilerTarget
    public sealed interface Apple : CxCompilerTarget
    public sealed interface Macos : Apple, Desktop
    public sealed interface Ios : Apple

    public val primitiveDataTypeSizes: Map<CxPrimitiveDataType, Int>

    public fun sizeOf(type: CxPrimitiveDataType): Int = primitiveDataTypeSizes.getValue(type)

    @Serializable
    public data object MacosArm64 : Macos {
        override val primitiveDataTypeSizes: Map<CxPrimitiveDataType, Int> = primitiveDataTypeSizes(
            longSize = Long.SIZE_BYTES,
            longDoubleSize = Long.SIZE_BYTES
        )
    }

    @Serializable
    public data object MacosX64 : Macos {
        override val primitiveDataTypeSizes: Map<CxPrimitiveDataType, Int> = primitiveDataTypeSizes(
            longSize = Long.SIZE_BYTES,
            longDoubleSize = Long.SIZE_BYTES * 2
        )
    }

    @Serializable
    public data object LinuxX64 : Desktop {
        override val primitiveDataTypeSizes: Map<CxPrimitiveDataType, Int> = primitiveDataTypeSizes(
            longSize = Long.SIZE_BYTES,
            longDoubleSize = Long.SIZE_BYTES * 2
        )
    }

    @Serializable
    public data object MingwX64 : Desktop {
        override val primitiveDataTypeSizes: Map<CxPrimitiveDataType, Int> = primitiveDataTypeSizes(
            longSize = Int.SIZE_BYTES,
            longDoubleSize = Long.SIZE_BYTES * 2
        )
    }

    @Serializable
    public data object IosDeviceArm64 : Ios {
        override val primitiveDataTypeSizes: Map<CxPrimitiveDataType, Int> = primitiveDataTypeSizes(
            longSize = Long.SIZE_BYTES,
            longDoubleSize = Long.SIZE_BYTES
        )
    }

    @Serializable
    public data object IosSimulatorArm64 : Ios {
        override val primitiveDataTypeSizes: Map<CxPrimitiveDataType, Int> = primitiveDataTypeSizes(
            longSize = Long.SIZE_BYTES,
            longDoubleSize = Long.SIZE_BYTES
        )
    }

    @Serializable
    public data object IosSimulatorX64 : Ios {
        override val primitiveDataTypeSizes: Map<CxPrimitiveDataType, Int> = primitiveDataTypeSizes(
            longSize = Long.SIZE_BYTES,
            longDoubleSize = Long.SIZE_BYTES * 2
        )
    }
}

private fun primitiveDataTypeSizes(
    longSize: Int,
    longDoubleSize: Int
): Map<CxPrimitiveDataType, Int> = mapOf(
    CxPrimitiveDataType.Void to 0,
    CxPrimitiveDataType.Bool to Byte.SIZE_BYTES,
    CxPrimitiveDataType.Char to Byte.SIZE_BYTES,
    CxPrimitiveDataType.UnsignedChar to Byte.SIZE_BYTES,
    CxPrimitiveDataType.SignedChar to Byte.SIZE_BYTES,
    CxPrimitiveDataType.Short to Short.SIZE_BYTES,
    CxPrimitiveDataType.UnsignedShort to Short.SIZE_BYTES,
    CxPrimitiveDataType.Int to Int.SIZE_BYTES,
    CxPrimitiveDataType.UnsignedInt to Int.SIZE_BYTES,
    CxPrimitiveDataType.Long to longSize,
    CxPrimitiveDataType.UnsignedLong to longSize,
    CxPrimitiveDataType.LongLong to Long.SIZE_BYTES,
    CxPrimitiveDataType.UnsignedLongLong to Long.SIZE_BYTES,
    CxPrimitiveDataType.Float to Int.SIZE_BYTES,
    CxPrimitiveDataType.Double to Long.SIZE_BYTES,
    CxPrimitiveDataType.LongDouble to longDoubleSize,
    CxPrimitiveDataType.Int128 to Long.SIZE_BYTES * 2,
    CxPrimitiveDataType.UnsignedInt128 to Long.SIZE_BYTES * 2
)
