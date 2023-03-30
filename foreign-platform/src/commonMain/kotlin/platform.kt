package dev.whyoleg.foreign.platform

public expect class PlatformInt : Comparable<PlatformInt> {
    public companion object {
        public val SIZE_BYTES: Int
    }
}

public expect value class PlatformUInt internal constructor(internal val data: PlatformInt) : Comparable<PlatformUInt>

public expect inline fun PlatformInt.toPlatformUInt(): PlatformUInt
public expect inline fun PlatformUInt.toPlatformInt(): PlatformInt
