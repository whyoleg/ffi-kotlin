package dev.whyoleg.foreign.memory

import java.lang.foreign.*
import java.lang.foreign.MemorySegment as JMemorySegment

//@ForeignMemoryApi
//public inline fun foreignMemory(block: (allocator: SegmentAllocator) -> MemoryAddress): MemorySegment {
//    return MemorySegment(block(FFI.autoAllocator))
//}

@ForeignMemoryApi
public actual class MemorySegment
internal constructor(
    private val segment: JMemorySegment
) {
    public actual val address: MemoryAddress get() = segment
    public actual val size: MemoryAddressSize get() = segment.byteSize()
    public actual val isAccessible: Boolean get() = segment.scope().isAlive

    public actual fun loadByte(offset: MemoryAddressSize): Byte = segment.get(ValueLayout.JAVA_BYTE, offset)
    public actual fun storeByte(offset: MemoryAddressSize, value: Byte): Unit = segment.set(ValueLayout.JAVA_BYTE, offset, value)

    public actual fun loadInt(offset: MemoryAddressSize): Int = segment.get(ValueLayout.JAVA_INT, offset)
    public actual fun storeInt(offset: MemoryAddressSize, value: Int): Unit = segment.set(ValueLayout.JAVA_INT, offset, value)

    public actual fun loadLong(offset: MemoryAddressSize): Long = segment.get(ValueLayout.JAVA_LONG, offset)
    public actual fun storeLong(offset: MemoryAddressSize, value: Long): Unit = segment.set(ValueLayout.JAVA_LONG, offset, value)

    public actual fun loadString(offset: MemoryAddressSize): String = segment.getUtf8String(offset)
    public actual fun storeString(offset: MemoryAddressSize, value: String): Unit = segment.setUtf8String(offset, value)

    public actual fun loadByteArray(offset: MemoryAddressSize, array: ByteArray, arrayStartIndex: Int, arrayEndIndex: Int) {
        JMemorySegment.copy(
            /*src*/ segment, ValueLayout.JAVA_BYTE, offset,
            /*dst*/ array, arrayStartIndex,
            arrayEndIndex - arrayStartIndex
        )
    }

    public actual fun storeByteArray(offset: MemoryAddressSize, array: ByteArray, arrayStartIndex: Int, arrayEndIndex: Int) {
        JMemorySegment.copy(
            /*src*/ array, arrayStartIndex,
            /*dst*/ segment, ValueLayout.JAVA_BYTE, offset,
            arrayEndIndex - arrayStartIndex
        )
    }

    public actual fun loadPointed(offset: MemoryAddressSize, pointedLayout: MemoryLayout): MemorySegment? {
        return fromAddress(segment.get(ValueLayout.ADDRESS, offset), pointedLayout.size, SegmentScope.auto())
    }

    public actual fun storePointed(offset: MemoryAddressSize, pointedLayout: MemoryLayout, value: MemorySegment?) {
        segment.set(ValueLayout.ADDRESS, offset, value?.segment ?: JMemorySegment.NULL)
    }

    public actual fun loadSegment(offset: MemoryAddressSize, valueLayout: MemoryLayout): MemorySegment {
        return MemorySegment(segment.asSlice(offset, valueLayout.size))
    }

    public actual fun storeSegment(offset: MemoryAddressSize, valueLayout: MemoryLayout, value: MemorySegment) {
        JMemorySegment.copy(
            /* srcSegment = */ value.segment,
            /* srcOffset = */ 0,
            /* dstSegment = */ segment,
            /* dstOffset = */ offset,
            /* bytes = */ valueLayout.size
        )
    }

    private fun resize(size: MemoryAddressSize): MemorySegment = MemorySegment(segment.resize(size))

    public actual fun resize(layout: MemoryLayout): MemorySegment = resize(layout.size)
    public actual fun resize(elementLayout: MemoryLayout, elementsCount: Int): MemorySegment = resize(elementLayout.size * elementsCount)
    public actual fun asUnbounded(): MemorySegment = resize(Long.MAX_VALUE)

    public actual companion object {
        public actual val Empty: MemorySegment = MemorySegment(JMemorySegment.NULL)

        internal fun fromAddress(address: MemoryAddress, size: MemoryAddressSize, scope: SegmentScope): MemorySegment? {
            if (address == JMemorySegment.NULL) return null
            return MemorySegment(address.resize(size, scope))
        }
    }
}

@ForeignMemoryApi
private fun JMemorySegment.resize(size: MemoryAddressSize, scope: SegmentScope = scope()): JMemorySegment {
    return JMemorySegment.ofAddress(address(), size, scope)
}
