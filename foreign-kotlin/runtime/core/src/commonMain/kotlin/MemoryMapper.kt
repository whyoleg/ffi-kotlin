package dev.whyoleg.foreign

// KT = primitive, address or record
public interface MemoryMapper<KT> {
    public fun getValue(memoryBlock: MemoryBlock, offset: MemorySizeInt): KT
    public fun setValue(memoryBlock: MemoryBlock, offset: MemorySizeInt, value: KT)

    @Suppress("FunctionName")
    public companion object {
        public fun Byte(): MemoryMapper<Byte> = ByteMemoryMapper
        public fun Int(): MemoryMapper<Int> = TODO()
        public fun UInt(): MemoryMapper<UInt> = TODO()
        public fun MemorySizeInt(): MemoryMapper<MemorySizeInt> = TODO()
        public fun PlatformInt(): MemoryMapper<PlatformInt> = TODO()
    }
}

internal object ByteMemoryMapper : MemoryMapper<Byte> {
    override fun getValue(memoryBlock: MemoryBlock, offset: MemorySizeInt): Byte =
        memoryBlock.getByte(offset)

    override fun setValue(memoryBlock: MemoryBlock, offset: MemorySizeInt, value: Byte): Unit =
        memoryBlock.setByte(offset, value)
}
