package dev.whyoleg.foreign.memory

import kotlinx.cinterop.*
import kotlin.native.internal.*
import kotlin.native.internal.NativePtr

//TODO: decide on how to work with cleaner
@Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")
@ForeignMemoryApi
internal class NativeMemoryBlock(
    private val pointer: NativePtr,
    override val size: MemoryAddressSize,
    private val cleaner: Cleaner?
) : MemoryBlock() {
    override val address: MemoryAddressSize get() = pointer.toLong()

    private var _isAccessible = true
    override val isAccessible: Boolean get() = _isAccessible

    internal fun makeInaccessible() {
        _isAccessible = false
    }

    private inline fun checkAccessFor(offset: MemoryAddressSize, bytes: MemoryAddressSize) {
        check(isAccessible) { "Not accessible" }
        check(offset + bytes <= size) { "Out of bound access [offset=$offset, bytes=$bytes, size=$size]" }
    }

    private inline fun pointed(offset: MemoryAddressSize): NativePointed = interpretOpaquePointed(pointer + offset)
    private inline fun <T : CPointed> pointer(offset: MemoryAddressSize): CPointer<T> = interpretCPointer(pointer + offset)!!

    override fun loadByte(offset: MemoryAddressSize): Byte {
        checkAccessFor(offset, 1)
        return nativeMemUtils.getByte(pointed(offset))
    }

    override fun storeByte(offset: MemoryAddressSize, value: Byte) {
        checkAccessFor(offset, 1)
        nativeMemUtils.putByte(pointed(offset), value)
    }

    override fun loadInt(offset: MemoryAddressSize): Int {
        checkAccessFor(offset, 4)
        return nativeMemUtils.getInt(pointed(offset))
    }

    override fun storeInt(offset: MemoryAddressSize, value: Int) {
        checkAccessFor(offset, 4)
        nativeMemUtils.putInt(pointed(offset), value)
    }

    override fun loadLong(offset: MemoryAddressSize): Long {
        checkAccessFor(offset, 8)
        return nativeMemUtils.getLong(pointed(offset))
    }

    override fun storeLong(offset: MemoryAddressSize, value: Long) {
        checkAccessFor(offset, 8)
        nativeMemUtils.putLong(pointed(offset), value)
    }

    override fun loadString(offset: MemoryAddressSize, unsafe: Boolean): String {
        //TODO: check unsafe
        checkAccessFor(offset, 1) //TODO: check at least one?
        return pointer<ByteVar>(offset).toKString()
    }

    override fun storeString(offset: MemoryAddressSize, value: String) {
        val bytes = value.encodeToByteArray()
        checkAccessFor(offset, (bytes.size + 1).toLong())
        nativeMemUtils.putByteArray(bytes, pointed(offset), bytes.size)
        nativeMemUtils.putByte(pointed(offset + bytes.size), 0)
    }

    override fun loadByteArray(offset: MemoryAddressSize, array: ByteArray, arrayStartIndex: Int, arrayEndIndex: Int) {
        val size = arrayEndIndex - arrayStartIndex
        checkAccessFor(offset, size.toLong())
        val temp = ByteArray(size)
        nativeMemUtils.getByteArray(pointed(offset), temp, size)
        temp.copyInto(array, arrayStartIndex)
    }

    override fun storeByteArray(offset: MemoryAddressSize, array: ByteArray, arrayStartIndex: Int, arrayEndIndex: Int) {
        val size = arrayEndIndex - arrayStartIndex
        checkAccessFor(offset, size.toLong())
        nativeMemUtils.putByteArray(array.copyOfRange(arrayStartIndex, arrayEndIndex), pointed(offset), size)
    }

    override fun loadPointed(offset: MemoryAddressSize, pointedLayout: MemoryBlockLayout): MemoryBlock? {
        //TODO: use sizeOf?
        checkAccessFor(offset, 8) //TODO?
        return fromAddress(nativeMemUtils.getNativePtr(pointed(offset)), pointedLayout)
    }

    override fun storePointed(offset: MemoryAddressSize, pointedLayout: MemoryBlockLayout, value: MemoryBlock?) {
        //TODO: use sizeOf?
        checkAccessFor(offset, 8) //TODO?
        nativeMemUtils.putNativePtr(pointed(offset), (value as? NativeMemoryBlock)?.pointer ?: NativePtr.NULL)
    }

    override fun loadSegment(offset: MemoryAddressSize, valueLayout: MemoryBlockLayout): MemoryBlock {
        checkAccessFor(offset, valueLayout.size)
        return NativeMemoryBlock(pointer + offset, valueLayout.size, cleaner)
    }

    override fun storeSegment(offset: MemoryAddressSize, valueLayout: MemoryBlockLayout, value: MemoryBlock) {
        value as NativeMemoryBlock
        checkAccessFor(offset, valueLayout.size)
        value.checkAccessFor(0, valueLayout.size)
        nativeMemUtils.copyMemory(pointed(offset), valueLayout.size.toInt(), value.pointed(0))
    }

    private fun resize(size: MemoryAddressSize): MemoryBlock = NativeMemoryBlock(pointer, size, cleaner)

    override fun resize(layout: MemoryBlockLayout): MemoryBlock = resize(layout.size)
    override fun resize(elementLayout: MemoryBlockLayout, elementsCount: Int): MemoryBlock = resize(elementLayout.size * elementsCount)

    companion object {
        val NULL: MemoryBlock get() = NativeMemoryBlock(NativePtr.NULL, 0, null)

        internal fun fromAddress(address: NativePtr, layout: MemoryBlockLayout): MemoryBlock? {
            if (address == NativePtr.NULL) return null
            return NativeMemoryBlock(address, layout.size, null)
        }
    }
}
