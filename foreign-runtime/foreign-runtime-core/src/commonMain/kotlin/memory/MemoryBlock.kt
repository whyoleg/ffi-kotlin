package dev.whyoleg.foreign.memory

import dev.whyoleg.foreign.platform.*

@ForeignMemoryApi
public abstract class MemoryBlock internal constructor() {
    public abstract val address: MemoryAddressSize
    public abstract val size: MemoryAddressSize

    // returns false, when block is released
    public abstract val isAccessible: Boolean

    public abstract fun loadByte(offset: MemoryAddressSize): Byte
    public abstract fun storeByte(offset: MemoryAddressSize, value: Byte)

    public abstract fun loadInt(offset: MemoryAddressSize): Int
    public abstract fun storeInt(offset: MemoryAddressSize, value: Int)

    public abstract fun loadLong(offset: MemoryAddressSize): Long
    public abstract fun storeLong(offset: MemoryAddressSize, value: Long)

    //TODO: decide on better way to get unsafe strings
    public abstract fun loadString(offset: MemoryAddressSize, unsafe: Boolean): String
    public abstract fun storeString(offset: MemoryAddressSize, value: String)

    //TODO: decide on parameters!!!
    public abstract fun loadByteArray(
        offset: MemoryAddressSize,
        array: ByteArray,
        arrayStartIndex: Int = 0,
        arrayEndIndex: Int = array.size
    )

    public abstract fun storeByteArray(
        offset: MemoryAddressSize,
        array: ByteArray,
        arrayStartIndex: Int = 0,
        arrayEndIndex: Int = array.size
    )

    // TODO: rename functions?
    // TODO: need to add cleanup action here!!!
    public abstract fun loadPointed(offset: MemoryAddressSize, pointedLayout: MemoryBlockLayout): MemoryBlock?
    public abstract fun storePointed(offset: MemoryAddressSize, pointedLayout: MemoryBlockLayout, value: MemoryBlock?)

    // TODO: rename functions?
    // load - creates a view
    // store - copy
    public abstract fun loadSegment(offset: MemoryAddressSize, valueLayout: MemoryBlockLayout): MemoryBlock
    public abstract fun storeSegment(offset: MemoryAddressSize, valueLayout: MemoryBlockLayout, value: MemoryBlock)

    // returned blocks in those calls should be linked to the same scope
    //TODO: naming
    public abstract fun resize(layout: MemoryBlockLayout): MemoryBlock
    public abstract fun resize(elementLayout: MemoryBlockLayout, elementsCount: Int): MemoryBlock

    public companion object {
        public val NULL: MemoryBlock = createNullMemoryBlock()
    }
}

@ForeignMemoryApi
public expect inline fun MemoryBlock.loadPlatformInt(offset: MemoryAddressSize): PlatformInt

@ForeignMemoryApi
public expect inline fun MemoryBlock.storePlatformInt(offset: MemoryAddressSize, value: PlatformInt)
