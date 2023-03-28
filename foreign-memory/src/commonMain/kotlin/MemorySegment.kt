package dev.whyoleg.foreign.memory

import dev.whyoleg.foreign.platform.*

@ForeignMemoryApi
public expect class MemorySegment {
//    public val address: MemoryAddressSize
//    public val size: MemoryAddressSize

    public fun loadByte(offset: MemoryAddressSize): Byte
    public fun storeByte(offset: MemoryAddressSize, value: Byte)

    public fun loadInt(offset: MemoryAddressSize): Int
    public fun storeInt(offset: MemoryAddressSize, value: Int)

    public fun loadLong(offset: MemoryAddressSize): Long
    public fun storeLong(offset: MemoryAddressSize, value: Long)

    public fun loadPlatformInt(offset: MemoryAddressSize): PlatformInt
    public fun storePlatformInt(offset: MemoryAddressSize, value: PlatformInt)

    public fun loadString(offset: MemoryAddressSize): String
    public fun storeString(offset: MemoryAddressSize, value: String)

    public fun loadAddress(offset: MemoryAddressSize, pointedLayout: MemoryLayout): MemorySegment?
    public fun storeAddress(offset: MemoryAddressSize, pointedLayout: MemoryLayout, value: MemorySegment?)

    public fun loadSegment(offset: MemoryAddressSize, segmentLayout: MemoryLayout): MemorySegment
    public fun storeSegment(offset: MemoryAddressSize, segmentLayout: MemoryLayout, value: MemorySegment)

    public fun view(offset: MemoryAddressSize, layout: MemoryLayout): MemorySegment

    public companion object {
        public val Empty: MemorySegment
    }
}
