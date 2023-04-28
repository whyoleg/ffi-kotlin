package dev.whyoleg.foreign.ffm

import dev.whyoleg.foreign.memory.*
import java.lang.foreign.*

@ForeignMemoryApi
internal class FfmMemoryBlock(internal val segment: MemorySegment) : MemoryBlock() {
    override val address: MemoryAddressSize get() = segment.address()
    override val size: MemoryAddressSize get() = segment.byteSize()
    override val isAccessible: Boolean get() = segment.scope().isAlive

    override fun loadByte(offset: MemoryAddressSize): Byte = segment.get(ValueLayout.JAVA_BYTE, offset)
    override fun storeByte(offset: MemoryAddressSize, value: Byte): Unit = segment.set(ValueLayout.JAVA_BYTE, offset, value)

    override fun loadInt(offset: MemoryAddressSize): Int = segment.get(ValueLayout.JAVA_INT, offset)
    override fun storeInt(offset: MemoryAddressSize, value: Int): Unit = segment.set(ValueLayout.JAVA_INT, offset, value)

    override fun loadLong(offset: MemoryAddressSize): Long = segment.get(ValueLayout.JAVA_LONG, offset)
    override fun storeLong(offset: MemoryAddressSize, value: Long): Unit = segment.set(ValueLayout.JAVA_LONG, offset, value)

    override fun loadString(offset: MemoryAddressSize, unsafe: Boolean): String {
        return (if (unsafe) segment.resize(Long.MAX_VALUE) else segment).getUtf8String(offset)
    }

    override fun storeString(offset: MemoryAddressSize, value: String): Unit = segment.setUtf8String(offset, value)

    override fun loadByteArray(offset: MemoryAddressSize, array: ByteArray, arrayStartIndex: Int, arrayEndIndex: Int) {
        MemorySegment.copy(
            /*src*/ segment, ValueLayout.JAVA_BYTE, offset,
            /*dst*/ array, arrayStartIndex,
            arrayEndIndex - arrayStartIndex
        )
    }

    override fun storeByteArray(offset: MemoryAddressSize, array: ByteArray, arrayStartIndex: Int, arrayEndIndex: Int) {
        MemorySegment.copy(
            /*src*/ array, arrayStartIndex,
            /*dst*/ segment, ValueLayout.JAVA_BYTE, offset,
            arrayEndIndex - arrayStartIndex
        )
    }

    override fun loadPointed(offset: MemoryAddressSize, pointedLayout: MemoryBlockLayout): MemoryBlock? {
        return fromAddress(segment.get(ValueLayout.ADDRESS, offset), pointedLayout.size, SegmentScope.auto())
    }

    override fun storePointed(offset: MemoryAddressSize, pointedLayout: MemoryBlockLayout, value: MemoryBlock?) {
        segment.set(ValueLayout.ADDRESS, offset, value?.ffmMemorySegment() ?: MemorySegment.NULL)
    }

    override fun loadSegment(offset: MemoryAddressSize, valueLayout: MemoryBlockLayout): MemoryBlock {
        return FfmMemoryBlock(segment.asSlice(offset, valueLayout.size))
    }

    override fun storeSegment(offset: MemoryAddressSize, valueLayout: MemoryBlockLayout, value: MemoryBlock) {
        MemorySegment.copy(
            /* srcSegment = */ value.ffmMemorySegment(),
            /* srcOffset = */ 0,
            /* dstSegment = */ segment,
            /* dstOffset = */ offset,
            /* bytes = */ valueLayout.size
        )
    }

    private fun resize(size: MemoryAddressSize): MemoryBlock = FfmMemoryBlock(segment.resize(size))

    override fun resize(layout: MemoryBlockLayout): MemoryBlock = resize(layout.size)
    override fun resize(elementLayout: MemoryBlockLayout, elementsCount: Int): MemoryBlock = resize(elementLayout.size * elementsCount)

    companion object {
        internal val NULL get() = FfmMemoryBlock(MemorySegment.NULL)

        internal fun fromAddress(address: MemorySegment, size: MemoryAddressSize, scope: SegmentScope): MemoryBlock? {
            if (address == MemorySegment.NULL) return null
            return FfmMemoryBlock(address.resize(size, scope))
        }

        internal fun fromAddress(address: MemoryAddressSize, size: MemoryAddressSize, scope: SegmentScope): MemoryBlock? {
            if (address == 0L) return null
            return FfmMemoryBlock(MemorySegment.ofAddress(address, size, scope))
        }
    }
}

@ForeignMemoryApi
private fun MemorySegment.resize(size: MemoryAddressSize, scope: SegmentScope = scope()): MemorySegment {
    return MemorySegment.ofAddress(address(), size, scope)
}
