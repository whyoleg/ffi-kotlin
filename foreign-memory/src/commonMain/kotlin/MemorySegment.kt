package dev.whyoleg.foreign.memory

import dev.whyoleg.foreign.platform.*

//TODO: rename to MemoryView?
@ForeignMemoryApi
public expect class MemorySegment {
    public val address: MemoryAddressSize
    public val size: MemoryAddressSize

    // returns false, when segment is released
    public val isAccessible: Boolean

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

    public fun loadByteArray(offset: MemoryAddressSize, array: ByteArray, arrayStartIndex: Int = 0, arrayEndIndex: Int = array.size)
    public fun storeByteArray(offset: MemoryAddressSize, array: ByteArray, arrayStartIndex: Int = 0, arrayEndIndex: Int = array.size)

    // TODO: rename functions?
    // TODO: need to add cleanup action here!!!
    public fun loadPointed(offset: MemoryAddressSize, pointedLayout: MemoryLayout): MemorySegment?
    public fun storePointed(offset: MemoryAddressSize, pointedLayout: MemoryLayout, value: MemorySegment?)

    // TODO: rename functions?
    // load - creates a view
    // store - copy
    public fun loadSegment(offset: MemoryAddressSize, valueLayout: MemoryLayout): MemorySegment
    public fun storeSegment(offset: MemoryAddressSize, valueLayout: MemoryLayout, value: MemorySegment)

    public companion object {
        public val Empty: MemorySegment
    }
}
