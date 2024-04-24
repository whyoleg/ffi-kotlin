package dev.whyoleg.foreign

// TODO: design and implement all available/possible operations
public expect class MemorySizeInt : Number, Comparable<MemorySizeInt> {
    public override operator fun compareTo(other: MemorySizeInt): Int

    override fun toByte(): Byte
    override fun toShort(): Short
    override fun toInt(): Int
    override fun toLong(): Long

    override fun toFloat(): Float
    override fun toDouble(): Double

    public operator fun plus(other: MemorySizeInt): MemorySizeInt
    public operator fun times(other: Int): MemorySizeInt
    public operator fun rem(other: Int): MemorySizeInt

    public companion object {
        public val SIZE_BYTES: Int
    }
}

public expect inline fun Int.toMemorySizeInt(): MemorySizeInt
public expect inline fun Long.toMemorySizeInt(): MemorySizeInt

public expect class PlatformInt : Number, Comparable<PlatformInt> {
    public override operator fun compareTo(other: PlatformInt): Int

    override fun toByte(): Byte
    override fun toShort(): Short
    override fun toInt(): Int
    override fun toLong(): Long
    override fun toFloat(): Float
    override fun toDouble(): Double

    public companion object {
        public val SIZE_BYTES: Int
    }
}

public expect inline fun Int.toPlatformInt(): PlatformInt
public expect inline fun Long.toPlatformInt(): PlatformInt

public expect value class PlatformUInt internal constructor(internal val data: PlatformInt) : Comparable<PlatformUInt> {
    public override operator fun compareTo(other: PlatformUInt): Int

    public fun toByte(): Byte
    public fun toShort(): Short
    public fun toInt(): Int
    public fun toLong(): Long
    public fun toFloat(): Float
    public fun toDouble(): Double

    public fun toUByte(): UByte
    public fun toUShort(): UShort
    public fun toUInt(): UInt
    public fun toULong(): ULong

    public companion object {
        public val SIZE_BYTES: Int
    }
}

public expect inline fun UInt.toPlatformUInt(): PlatformUInt
public expect inline fun ULong.toPlatformUInt(): PlatformUInt

//// TODO: where to put conversions (check stdlib)?
//public expect inline fun PlatformInt.toPlatformUInt(): PlatformUInt
//public expect inline fun PlatformUInt.toPlatformInt(): PlatformInt
