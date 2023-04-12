package dev.whyoleg.foreign.platform

@Suppress("VIRTUAL_MEMBER_HIDDEN")
public expect class PlatformInt : Number, Comparable<PlatformInt> {
    public operator fun compareTo(other: Byte): Int
    public operator fun compareTo(other: Short): Int
    public operator fun compareTo(other: Int): Int
    public operator fun compareTo(other: Long): Int

    public companion object {
        public val SIZE_BYTES: Int
    }
}

public expect value class PlatformUInt internal constructor(internal val data: PlatformInt) : Comparable<PlatformUInt> {
    public fun toInt(): Int
    public fun toLong(): Long

    public fun toUInt(): UInt
    public fun toULong(): ULong

    public companion object {

    }
}

public expect inline fun PlatformInt.toPlatformUInt(): PlatformUInt
public expect inline fun PlatformUInt.toPlatformInt(): PlatformInt

//TODO: is there will be boxing here?
public expect inline fun Number.toPlatformInt(): PlatformInt
public expect inline fun Number.toPlatformUInt(): PlatformUInt

