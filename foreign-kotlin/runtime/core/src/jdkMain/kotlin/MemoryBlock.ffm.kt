package dev.whyoleg.foreign

import java.lang.foreign.*
import java.lang.foreign.MemoryLayout

internal class FfmMemoryBlock(private val segment: MemorySegment) : InternalMemoryBlock {
    override val address: MemorySizeInt get() = segment.address()
    override val size: MemorySizeInt get() = segment.byteSize()
    override val isAccessible: Boolean get() = segment.scope().isAlive

    override fun getByte(offset: MemorySizeInt): Byte {
        ValueLayout.JAVA_BYTE.byteOffset(MemoryLayout.PathElement.groupElement(0))
        ValueLayout.JAVA_BYTE.varHandle(MemoryLayout.PathElement.groupElement(0))
        return segment.get(ValueLayout.JAVA_BYTE, offset)
    }

    override fun setByte(offset: MemorySizeInt, value: Byte) {
        segment.set(ValueLayout.JAVA_BYTE, offset, value)
    }

    override fun getInt(offset: MemorySizeInt): Int {
        return segment.get(ValueLayout.JAVA_INT, offset)
    }

    override fun setInt(offset: MemorySizeInt, value: Int) {
        segment.set(ValueLayout.JAVA_INT, offset, value)
    }

    override fun getLong(offset: MemorySizeInt): Long {
        return segment.get(ValueLayout.JAVA_LONG, offset)
    }

    override fun setLong(offset: MemorySizeInt, value: Long) {
        segment.set(ValueLayout.JAVA_LONG, offset, value)
    }

//    override fun getString(offset: MemorySizeInt, unsafe: Boolean): String {
//        return (if (unsafe) segment.resize(Long.MAX_VALUE) else segment).getUtf8String(offset)
//    }
//
//    override fun setString(offset: MemorySizeInt, value: String): Unit = segment.setUtf8String(offset, value)
//
//    override fun getByteArray(offset: MemorySizeInt, array: ByteArray, arrayStartIndex: Int, arrayEndIndex: Int) {
//        MemorySegment.copy(
//            /*src*/ segment, ValueLayout.JAVA_BYTE, offset,
//            /*dst*/ array, arrayStartIndex,
//            arrayEndIndex - arrayStartIndex
//        )
//    }
//
//    override fun setByteArray(offset: MemorySizeInt, array: ByteArray, arrayStartIndex: Int, arrayEndIndex: Int) {
//        MemorySegment.copy(
//            /*src*/ array, arrayStartIndex,
//            /*dst*/ segment, ValueLayout.JAVA_BYTE, offset,
//            arrayEndIndex - arrayStartIndex
//        )
//    }
//
//    override fun getPointed(offset: MemorySizeInt, pointedLayout: MemoryBlockLayout): MemoryBlock? {
//        return fromAddress(segment.get(ValueLayout.ADDRESS, offset), pointedLayout.size, SegmentScope.auto())
//    }
//
//    override fun setPointed(offset: MemorySizeInt, pointedLayout: MemoryBlockLayout, value: MemoryBlock?) {
//        segment.set(ValueLayout.ADDRESS, offset, value?.ffmMemorySegment() ?: MemorySegment.NULL)
//    }
//
//    override fun getSegment(offset: MemorySizeInt, valueLayout: MemoryBlockLayout): MemoryBlock {
//        return FfmMemoryBlock(segment.asSlice(offset, valueLayout.size))
//    }
//
//    override fun setSegment(offset: MemorySizeInt, valueLayout: MemoryBlockLayout, value: MemoryBlock) {
//        MemorySegment.copy(
//            /* srcSegment = */ value.ffmMemorySegment(),
//            /* srcOffset = */ 0,
//            /* dstSegment = */ segment,
//            /* dstOffset = */ offset,
//            /* bytes = */ valueLayout.size
//        )
//    }
//
//    private fun resize(size: MemorySizeInt): MemoryBlock = FfmMemoryBlock(segment.resize(size))
//
//    override fun resize(layout: MemoryBlockLayout): MemoryBlock = resize(layout.size)
//    override fun resize(elementLayout: MemoryBlockLayout, elementsCount: Int): MemoryBlock =
//        resize(elementLayout.size * elementsCount)
//
//    companion object {
//        internal val NULL get() = FfmMemoryBlock(MemorySegment.NULL)
//
//        internal fun fromAddress(address: MemorySegment, size: MemorySizeInt, scope: SegmentScope): MemoryBlock? {
//            if (address == MemorySegment.NULL) return null
//            return FfmMemoryBlock(address.resize(size, scope))
//        }
//
//        internal fun fromAddress(
//            address: MemorySizeInt,
//            size: MemorySizeInt,
//            scope: SegmentScope
//        ): MemoryBlock? {
//            if (address == 0L) return null
//            return FfmMemoryBlock(MemorySegment.ofAddress(address, size, scope))
//        }
//    }
}

//@ForeignMemoryApi
//private fun MemorySegment.resize(size: MemorySizeInt, scope: SegmentScope = scope()): MemorySegment {
//    return MemorySegment.ofAddress(address(), size, scope)
//}
