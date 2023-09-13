package dev.whyoleg.foreign.tooling.cx.compiler.model

import kotlinx.serialization.*

@Serializable
public sealed interface CxTarget {
    public sealed interface Host : CxTarget
    public sealed interface Apple : CxTarget
    public sealed interface Macos : Apple, Host
    public sealed interface Ios : Apple

    public val builtinTypeSizes: Map<CxBuiltinType, Int>

    public fun sizeOf(type: CxBuiltinType): Int = builtinTypeSizes.getValue(type)

    @Serializable
    public data object MacosArm64 : Macos {
        override val builtinTypeSizes: Map<CxBuiltinType, Int> = builtinTypeSizes(
            longSize = Long.SIZE_BYTES,
            longDoubleSize = Long.SIZE_BYTES
        )
    }

    @Serializable
    public data object MacosX64 : Macos {
        override val builtinTypeSizes: Map<CxBuiltinType, Int> = builtinTypeSizes(
            longSize = Long.SIZE_BYTES,
            longDoubleSize = Long.SIZE_BYTES * 2
        )
    }

    @Serializable
    public data object LinuxX64 : Host {
        override val builtinTypeSizes: Map<CxBuiltinType, Int> = builtinTypeSizes(
            longSize = Long.SIZE_BYTES,
            longDoubleSize = Long.SIZE_BYTES * 2
        )
    }

    @Serializable
    public data object MingwX64 : Host {
        override val builtinTypeSizes: Map<CxBuiltinType, Int> = builtinTypeSizes(
            longSize = Int.SIZE_BYTES,
            longDoubleSize = Long.SIZE_BYTES * 2
        )
    }

    @Serializable
    public data object IosDeviceArm64 : Ios {
        override val builtinTypeSizes: Map<CxBuiltinType, Int> = builtinTypeSizes(
            longSize = Long.SIZE_BYTES,
            longDoubleSize = Long.SIZE_BYTES
        )
    }

    @Serializable
    public data object IosSimulatorArm64 : Ios {
        override val builtinTypeSizes: Map<CxBuiltinType, Int> = builtinTypeSizes(
            longSize = Long.SIZE_BYTES,
            longDoubleSize = Long.SIZE_BYTES
        )
    }

    @Serializable
    public data object IosSimulatorX64 : Ios {
        override val builtinTypeSizes: Map<CxBuiltinType, Int> = builtinTypeSizes(
            longSize = Long.SIZE_BYTES,
            longDoubleSize = Long.SIZE_BYTES * 2
        )
    }
}

private fun builtinTypeSizes(
    longSize: Int,
    longDoubleSize: Int
): Map<CxBuiltinType, Int> = mapOf(
    CxBuiltinType.Char to Byte.SIZE_BYTES,
    CxBuiltinType.UnsignedChar to Byte.SIZE_BYTES,
    CxBuiltinType.SignedChar to Byte.SIZE_BYTES,
    CxBuiltinType.Short to Short.SIZE_BYTES,
    CxBuiltinType.UnsignedShort to Short.SIZE_BYTES,
    CxBuiltinType.Int to Int.SIZE_BYTES,
    CxBuiltinType.UnsignedInt to Int.SIZE_BYTES,
    CxBuiltinType.Long to longSize,
    CxBuiltinType.UnsignedLong to longSize,
    CxBuiltinType.LongLong to Long.SIZE_BYTES,
    CxBuiltinType.UnsignedLongLong to Long.SIZE_BYTES,
    CxBuiltinType.Float to Int.SIZE_BYTES,
    CxBuiltinType.Double to Long.SIZE_BYTES,
    CxBuiltinType.LongDouble to longDoubleSize,
    CxBuiltinType.Int128 to Long.SIZE_BYTES * 2,
    CxBuiltinType.UnsignedInt128 to Long.SIZE_BYTES * 2
)
