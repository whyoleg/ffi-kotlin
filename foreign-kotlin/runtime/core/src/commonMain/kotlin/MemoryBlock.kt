package dev.whyoleg.foreign

public sealed interface MemoryBlock {
    public val address: MemorySizeInt
    public val size: MemorySizeInt

    public val isAccessible: Boolean

    public fun getByte(offset: MemorySizeInt): Byte
    public fun setByte(offset: MemorySizeInt, value: Byte)

    public fun getInt(offset: MemorySizeInt): Int
    public fun setInt(offset: MemorySizeInt, value: Int)

    public fun getLong(offset: MemorySizeInt): Long
    public fun setLong(offset: MemorySizeInt, value: Long)
}
