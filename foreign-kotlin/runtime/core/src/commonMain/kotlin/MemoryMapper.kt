package dev.whyoleg.foreign

// KT = primitive, address or record
public sealed class MemoryMapper<KT> {
    public abstract fun getValue(block: MemoryBlock, offset: MemorySizeInt): KT
    public abstract fun setValue(block: MemoryBlock, offset: MemorySizeInt, value: KT)

    @Suppress("FunctionName")
    public companion object {
        public fun Byte(): MemoryMapper<Byte> = ByteMemoryMapper
        public fun Int(): MemoryMapper<Int> = TODO()
        public fun UInt(): MemoryMapper<UInt> = TODO()
        public fun MemorySizeInt(): MemoryMapper<MemorySizeInt> = TODO()
        public fun PlatformInt(): MemoryMapper<PlatformInt> = TODO()
    }
}

internal object ByteMemoryMapper : MemoryMapper<Byte>() {
    override fun getValue(block: MemoryBlock, offset: MemorySizeInt): Byte = block.getByte(offset)
    override fun setValue(block: MemoryBlock, offset: MemorySizeInt, value: Byte): Unit = block.setByte(offset, value)
}
