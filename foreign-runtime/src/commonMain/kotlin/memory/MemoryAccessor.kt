package dev.whyoleg.foreign.memory

@ForeignMemoryApi
public abstract class MemoryAccessor<KT> {
    public abstract val offset: MemoryAddressSize

    public abstract fun get(segment: MemorySegment): KT
    public abstract fun set(segment: MemorySegment, value: KT)

    public class Byte(override val offset: MemoryAddressSize) : MemoryAccessor<kotlin.Byte>() {
        override fun get(segment: MemorySegment): kotlin.Byte = segment.loadByte(offset)
        override fun set(segment: MemorySegment, value: kotlin.Byte): Unit = segment.storeByte(offset, value)
    }

    public class PlatformInt(override val offset: MemoryAddressSize) : MemoryAccessor<dev.whyoleg.foreign.PlatformInt>() {
        override fun get(segment: MemorySegment): dev.whyoleg.foreign.PlatformInt = segment.loadPlatformInt(offset)
        override fun set(segment: MemorySegment, value: dev.whyoleg.foreign.PlatformInt): Unit = segment.storePlatformInt(offset, value)
    }
}
