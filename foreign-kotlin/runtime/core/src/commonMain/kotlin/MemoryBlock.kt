package dev.whyoleg.foreign

public sealed interface MemoryBlock {
    public val address: MemorySizeInt
    public val size: MemorySizeInt

    public val isAccessible: Boolean

    public fun getByte(offset: MemorySizeInt): Byte
    public fun setByte(offset: MemorySizeInt, value: Byte)

    public fun getInt(offset: MemorySizeInt): Int
    public fun setInt(offset: MemorySizeInt, value: Int)

    public fun getUInt(offset: MemorySizeInt): UInt
    public fun setUInt(offset: MemorySizeInt, value: UInt)

    public fun getLong(offset: MemorySizeInt): Long
    public fun setLong(offset: MemorySizeInt, value: Long)

    // for pointers
    public fun getPointed(offset: MemorySizeInt, pointedLayout: MemoryLayout): MemoryBlock?
    public fun setPointedAddress(offset: MemorySizeInt, block: MemoryBlock?)

    // for structs
    // TODO
    // TODO: asSlice
    public fun slice(offset: MemorySizeInt, layout: MemoryLayout): MemoryBlock
    public fun copyFrom(offset: MemorySizeInt, block: MemoryBlock)
}

public fun Unsafe.memoryBlockAddress(block: MemoryBlock?): MemorySizeInt = TODO()
