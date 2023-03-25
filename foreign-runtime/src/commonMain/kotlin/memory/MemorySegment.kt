package dev.whyoleg.foreign.memory

import dev.whyoleg.foreign.*

@ForeignMemoryApi
public sealed class MemorySegment {
    public abstract val address: MemoryAddressSize
    public abstract val size: MemoryAddressSize

    public abstract fun loadByte(offset: MemoryAddressSize): Byte
    public abstract fun storeByte(offset: MemoryAddressSize, value: Byte)

    public abstract fun loadInt(offset: MemoryAddressSize): Int
    public abstract fun storeInt(offset: MemoryAddressSize, value: Int)

    public abstract fun loadLong(offset: MemoryAddressSize): Long
    public abstract fun storeLong(offset: MemoryAddressSize, value: Long)

    public abstract fun loadPlatformInt(offset: MemoryAddressSize): PlatformInt
    public abstract fun storePlatformInt(offset: MemoryAddressSize, value: PlatformInt)

    public abstract fun loadString(offset: MemoryAddressSize): String
    public abstract fun storeString(offset: MemoryAddressSize, value: String)

    public abstract fun loadAddress(offset: MemoryAddressSize, pointedLayout: MemoryLayout): MemorySegment?
    public abstract fun storeAddress(offset: MemoryAddressSize, pointedLayout: MemoryLayout, value: MemorySegment?)

    public abstract fun loadSegment(offset: MemoryAddressSize, layout: MemoryLayout): MemorySegment
    public abstract fun storeSegment(offset: MemoryAddressSize, layout: MemoryLayout, value: MemorySegment)
}
