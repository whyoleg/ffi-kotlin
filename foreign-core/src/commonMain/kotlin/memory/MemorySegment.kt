package dev.whyoleg.foreign.memory

import dev.whyoleg.foreign.platform.*

//TODO: really need to rename to avoid confusion with Panama naming...
//TODO: rename to MemoryView?
@ForeignMemoryApi
public expect class MemorySegment {
    public val address: MemoryAddress
    public val size: MemoryAddressSize

    // returns false, when segment is released
    public val isAccessible: Boolean

    public fun loadByte(offset: MemoryAddressSize): Byte
    public fun storeByte(offset: MemoryAddressSize, value: Byte)

    public fun loadInt(offset: MemoryAddressSize): Int
    public fun storeInt(offset: MemoryAddressSize, value: Int)

    public fun loadLong(offset: MemoryAddressSize): Long
    public fun storeLong(offset: MemoryAddressSize, value: Long)

    public fun loadString(offset: MemoryAddressSize): String
    public fun storeString(offset: MemoryAddressSize, value: String)

    //TODO: decide on parameters!!!
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

    // returned segments in those calls should be linked to the same scope
    //TODO: naming
    public fun resize(layout: MemoryLayout): MemorySegment
    public fun resize(elementLayout: MemoryLayout, elementsCount: Int): MemorySegment
    public fun asUnbounded(): MemorySegment

    public companion object {
        public val Empty: MemorySegment
    }
}

@ForeignMemoryApi
public expect inline fun MemorySegment.loadPlatformInt(offset: MemoryAddressSize): PlatformInt

@ForeignMemoryApi
public expect inline fun MemorySegment.storePlatformInt(offset: MemoryAddressSize, value: PlatformInt)
