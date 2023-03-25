package dev.whyoleg.foreign.memory

import dev.whyoleg.foreign.platform.*

@ForeignMemoryApi //TODO: rename to MemoryPointer
public sealed class MemorySegment {
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

    public abstract fun loadAddress(offset: MemoryAddressSize): MemorySegment?
    public abstract fun storeAddress(offset: MemoryAddressSize, value: MemorySegment?)

    //TODO: rename to loadPointed
    public abstract fun loadSegment(offset: MemoryAddressSize, layout: MemoryLayout<*>): MemorySegment
    public abstract fun storeSegment(offset: MemoryAddressSize, layout: MemoryLayout<*>, value: MemorySegment)
}
