package dev.whyoleg.foreign

import kotlinx.cinterop.*
import kotlin.experimental.*

//TODO: decide on how to work with cleaner
@OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)
internal class NativeMemoryBlock(
    private val pointed: NativePointed,
    override val size: MemorySizeInt,
    //private val cleaner: Cleaner?
) : InternalMemoryBlock {
    override val address: MemorySizeInt get() = pointed.rawPtr.toLong()

    private var _isAccessible = true
    override val isAccessible: Boolean get() = _isAccessible

    internal fun makeInaccessible() {
        _isAccessible = false
    }

    private fun checkAccessFor(offset: MemorySizeInt, bytes: MemorySizeInt) {
        check(isAccessible) { "Not accessible" }
        check(offset + bytes <= size) { "Out of bound access [offset=$offset, bytes=$bytes, size=$size]" }
    }

    private inline fun <reified T : CVariable> pointed(offset: MemorySizeInt): T {
        checkAccessFor(offset, sizeOf<T>())
        return interpretOpaquePointed(pointed.rawPtr + offset).reinterpret<T>()
    }

    //    private inline fun pointed(offset: MemorySizeInt): NativePointed = interpretOpaquePointed(pointed + offset)
//    private inline fun <T : CPointed> pointer(offset: MemorySizeInt): CPointer<T> =
//        interpretCPointer(pointed + offset)!!

    override fun getByte(offset: MemorySizeInt): Byte {
        return pointed<ByteVar>(offset).value
    }

    override fun setByte(offset: MemorySizeInt, value: Byte) {
        pointed<ByteVar>(offset).value = value
    }

    override fun getInt(offset: MemorySizeInt): Int {
        return pointed<IntVar>(offset).value
    }

    override fun setInt(offset: MemorySizeInt, value: Int) {
        pointed<IntVar>(offset).value = value
    }

    override fun getLong(offset: MemorySizeInt): Long {
        return pointed<LongVar>(offset).value
    }

    override fun setLong(offset: MemorySizeInt, value: Long) {
        pointed<LongVar>(offset).value = value
    }

//    override fun getString(offset: MemorySizeInt, unsafe: Boolean): String {
//        //TODO: check unsafe
//        checkAccessFor(offset, 1) //TODO: check at least one?
//        return pointer<ByteVar>(offset).toKString()
//    }
//
//    override fun setString(offset: MemorySizeInt, value: String) {
//        val bytes = value.encodeToByteArray()
//        checkAccessFor(offset, (bytes.size + 1).toLong())
//        nativeMemUtils.putByteArray(bytes, pointed(offset), bytes.size)
//        nativeMemUtils.putByte(pointed(offset + bytes.size), 0)
//    }
//
//    override fun getByteArray(offset: MemorySizeInt, array: ByteArray, arrayStartIndex: Int, arrayEndIndex: Int) {
//        val size = arrayEndIndex - arrayStartIndex
//        checkAccessFor(offset, size.toLong())
//        val temp = ByteArray(size)
//        nativeMemUtils.getByteArray(pointed(offset), temp, size)
//        temp.copyInto(array, arrayStartIndex)
//    }
//
//    override fun setByteArray(offset: MemorySizeInt, array: ByteArray, arrayStartIndex: Int, arrayEndIndex: Int) {
//        val size = arrayEndIndex - arrayStartIndex
//        checkAccessFor(offset, size.toLong())
//        nativeMemUtils.putByteArray(array.copyOfRange(arrayStartIndex, arrayEndIndex), pointed(offset), size)
//    }
//
//    override fun getPointed(offset: MemorySizeInt, pointedLayout: MemoryBlockLayout): MemoryBlock? {
//        //TODO: use sizeOf?
//        checkAccessFor(offset, 8) //TODO?
//        return fromAddress(nativeMemUtils.getNativePtr(pointed(offset)), pointedLayout)
//    }
//
//    override fun setPointed(offset: MemorySizeInt, pointedLayout: MemoryBlockLayout, value: MemoryBlock?) {
//        //TODO: use sizeOf?
//        checkAccessFor(offset, 8) //TODO?
//        nativeMemUtils.putNativePtr(pointed(offset), (value as? NativeMemoryBlock)?.pointer ?: NativePtr.NULL)
//    }
//
//    override fun getSegment(offset: MemorySizeInt, valueLayout: MemoryBlockLayout): MemoryBlock {
//        checkAccessFor(offset, valueLayout.size)
//        return NativeMemoryBlock(pointer + offset, valueLayout.size, cleaner)
//    }
//
//    override fun setSegment(offset: MemorySizeInt, valueLayout: MemoryBlockLayout, value: MemoryBlock) {
//        value as NativeMemoryBlock
//        checkAccessFor(offset, valueLayout.size)
//        value.checkAccessFor(0, valueLayout.size)
//        nativeMemUtils.copyMemory(pointed(offset), valueLayout.size.toInt(), value.pointed(0))
//    }
//
//    private fun resize(size: MemorySizeInt): MemoryBlock = NativeMemoryBlock(pointer, size, cleaner)
//
//    override fun resize(layout: MemoryBlockLayout): MemoryBlock = resize(layout.size)
//    override fun resize(elementLayout: MemoryBlockLayout, elementsCount: Int): MemoryBlock =
//        resize(elementLayout.size * elementsCount)
//
//    companion object {
//        val NULL: MemoryBlock get() = NativeMemoryBlock(NativePtr.NULL, 0, null)
//
//        internal fun fromAddress(address: NativePtr, layout: MemoryBlockLayout): MemoryBlock? {
//            if (address == NativePtr.NULL) return null
//            return NativeMemoryBlock(address, layout.size, null)
//        }
//    }
}
