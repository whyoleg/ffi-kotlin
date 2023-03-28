package dev.whyoleg.foreign.memory

import dev.whyoleg.foreign.platform.*
import kotlinx.cinterop.*
import kotlinx.cinterop.NativePtr
import kotlin.native.internal.*

//TODO: decide on how to work with cleaner
@Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")
@ForeignMemoryApi
public actual class MemorySegment internal constructor(
    private val pointer: NativePtr,
    public actual val size: MemoryAddressSize,
    private val cleaner: Cleaner?
) {
    public actual val address: MemoryAddress get() = pointer

    private var _isAccessible = true
    public actual val isAccessible: Boolean get() = _isAccessible

    internal fun makeInaccessible() {
        _isAccessible = false
    }

    private inline fun checkAccessFor(offset: MemoryAddressSize, bytes: MemoryAddressSize) {
        check(isAccessible) { "Not accessible" }
        check(offset + bytes < size) { "Out of bound access [offset=$offset, bytes=$bytes, size=$size]" }
    }

    private inline fun pointed(offset: MemoryAddressSize): NativePointed = interpretOpaquePointed(pointer + offset)
    private inline fun <T : CPointed> pointer(offset: MemoryAddressSize): CPointer<T> = interpretCPointer(pointer + offset)!!

    public actual fun loadByte(offset: MemoryAddressSize): Byte {
        checkAccessFor(offset, 1)
        return nativeMemUtils.getByte(pointed(offset))
    }

    public actual fun storeByte(offset: MemoryAddressSize, value: Byte) {
        checkAccessFor(offset, 1)
        nativeMemUtils.putByte(pointed(offset), value)
    }

    public actual fun loadInt(offset: MemoryAddressSize): Int {
        checkAccessFor(offset, 4)
        return nativeMemUtils.getInt(pointed(offset))
    }

    public actual fun storeInt(offset: MemoryAddressSize, value: Int) {
        checkAccessFor(offset, 4)
        nativeMemUtils.putInt(pointed(offset), value)
    }

    public actual fun loadLong(offset: MemoryAddressSize): Long {
        checkAccessFor(offset, 8)
        return nativeMemUtils.getLong(pointed(offset))
    }

    public actual fun storeLong(offset: MemoryAddressSize, value: Long) {
        checkAccessFor(offset, 8)
        nativeMemUtils.putLong(pointed(offset), value)
    }

    public actual fun loadPlatformInt(offset: MemoryAddressSize): PlatformInt = TODO()
    public actual fun storePlatformInt(offset: MemoryAddressSize, value: PlatformInt): Unit = TODO()

    public actual fun loadString(offset: MemoryAddressSize): String {
        checkAccessFor(offset, 1) //TODO: check at least one?
        return pointer<ByteVar>(offset).toKString()
    }

    public actual fun storeString(offset: MemoryAddressSize, value: String) {
        val bytes = value.encodeToByteArray()
        checkAccessFor(offset, (bytes.size + 1).toLong())
        nativeMemUtils.putByteArray(bytes, pointed(offset), bytes.size)
        nativeMemUtils.putByte(pointed(offset + bytes.size), 0)
    }

    public actual fun loadByteArray(offset: MemoryAddressSize, array: ByteArray, arrayStartIndex: Int, arrayEndIndex: Int) {
        val size = arrayEndIndex - arrayStartIndex
        checkAccessFor(offset, size.toLong())
        val temp = ByteArray(size)
        nativeMemUtils.getByteArray(pointed(offset), temp, size)
        temp.copyInto(array, arrayStartIndex)
    }

    public actual fun storeByteArray(offset: MemoryAddressSize, array: ByteArray, arrayStartIndex: Int, arrayEndIndex: Int) {
        val size = arrayEndIndex - arrayStartIndex
        checkAccessFor(offset, size.toLong())
        nativeMemUtils.putByteArray(array.copyOfRange(arrayStartIndex, arrayEndIndex), pointed(offset), size)
    }

    public actual fun loadPointed(offset: MemoryAddressSize, pointedLayout: MemoryLayout): MemorySegment? {
        //TODO: use sizeOf?
        checkAccessFor(offset, 8) //TODO?
        val ptr = nativeMemUtils.getNativePtr(pointed(offset))
        if (ptr == NativePtr.NULL) return null
        return MemorySegment(ptr, pointedLayout.size, null)
    }

    public actual fun storePointed(offset: MemoryAddressSize, pointedLayout: MemoryLayout, value: MemorySegment?) {
        //TODO: use sizeOf?
        checkAccessFor(offset, 8) //TODO?
        nativeMemUtils.putNativePtr(pointed(offset), value?.pointer ?: NativePtr.NULL)
    }

    public actual fun loadSegment(offset: MemoryAddressSize, valueLayout: MemoryLayout): MemorySegment {
        checkAccessFor(offset, valueLayout.size)
        return MemorySegment(pointer + offset, valueLayout.size, cleaner)
    }

    public actual fun storeSegment(offset: MemoryAddressSize, valueLayout: MemoryLayout, value: MemorySegment) {
        checkAccessFor(offset, valueLayout.size)
        value.checkAccessFor(0, valueLayout.size)
        nativeMemUtils.copyMemory(pointed(offset), valueLayout.size.toInt(), value.pointed(0))
    }

    public actual companion object {
        public actual val Empty: MemorySegment = MemorySegment(NativePtr.NULL, 0, null)
    }
}
