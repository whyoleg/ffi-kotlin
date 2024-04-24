package dev.whyoleg.foreign

public sealed interface WebAssemblyMemory {
    // returns address
    public fun alloc(size: MemorySizeInt): MemorySizeInt
    public fun free(address: MemorySizeInt)

    public fun getByte(address: MemorySizeInt): Byte
    public fun setByte(address: MemorySizeInt, value: Byte)

    public fun getInt(address: MemorySizeInt): Int
    public fun setInt(address: MemorySizeInt, value: Int)
}
