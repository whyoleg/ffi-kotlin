package dev.whyoleg.foreign.memory

import java.nio.*

@ForeignMemoryApi
public actual class MemorySegment internal constructor(
    private val holder: BufferHolder
) {
    private inline val buffer: ByteBuffer get() = holder.access()

    public actual val address: MemoryAddress get() = JNI.getPointerFromByteBuffer(buffer)
    public actual val size: MemoryAddressSize get() = buffer.capacity().toLong()
    public actual val isAccessible: Boolean get() = holder.isAccessible

    //TODO: copy check access and offset from native
    public actual fun loadByte(offset: MemoryAddressSize): Byte = buffer.get(offset.toInt())
    public actual fun storeByte(offset: MemoryAddressSize, value: Byte) {
        buffer.put(offset.toInt(), value)
    }

    public actual fun loadInt(offset: MemoryAddressSize): Int = buffer.getInt(offset.toInt())
    public actual fun storeInt(offset: MemoryAddressSize, value: Int) {
        buffer.putInt(offset.toInt(), value)
    }

    public actual fun loadLong(offset: MemoryAddressSize): Long = buffer.getLong(offset.toInt())
    public actual fun storeLong(offset: MemoryAddressSize, value: Long) {
        buffer.putLong(offset.toInt(), value)
    }

    public actual fun loadString(offset: MemoryAddressSize): String {
        return JNI.getStringFromPointer(address + offset)!!
    }

    public actual fun storeString(offset: MemoryAddressSize, value: String) {
        val bytes = value.encodeToByteArray()
        storeByteArray(offset, bytes)
        storeByte(offset + bytes.size, 0)
    }

    public actual fun loadByteArray(offset: MemoryAddressSize, array: ByteArray, arrayStartIndex: Int, arrayEndIndex: Int) {
        buffer.clear()
        buffer.position(offset.toInt())
        buffer.get(array, arrayStartIndex, arrayEndIndex - arrayStartIndex)
    }

    public actual fun storeByteArray(offset: MemoryAddressSize, array: ByteArray, arrayStartIndex: Int, arrayEndIndex: Int) {
        buffer.clear()
        buffer.position(offset.toInt())
        buffer.put(array, arrayStartIndex, arrayEndIndex - arrayStartIndex)
    }

    public actual fun loadPointed(offset: MemoryAddressSize, pointedLayout: MemoryLayout): MemorySegment? {
        return fromAddress(loadLong(offset), pointedLayout)
    }

    public actual fun storePointed(offset: MemoryAddressSize, pointedLayout: MemoryLayout, value: MemorySegment?) {
        val address = value?.address ?: 0
        storeLong(offset, address)
    }

    public actual fun loadSegment(offset: MemoryAddressSize, valueLayout: MemoryLayout): MemorySegment {
        buffer.clear()
        buffer.position(offset.toInt())
        buffer.limit((offset + valueLayout.size).toInt())
        return MemorySegment(holder.view(buffer.slice()))
    }

    public actual fun storeSegment(offset: MemoryAddressSize, valueLayout: MemoryLayout, value: MemorySegment) {
        buffer.clear()
        buffer.position(offset.toInt())
        buffer.limit((offset + valueLayout.size).toInt())
        value.buffer.clear()
        buffer.put(value.buffer)
    }

    public actual companion object {
        public actual val Empty: MemorySegment = MemorySegment(BufferHolder.Root(ByteBuffer.allocateDirect(0)))

        internal fun fromAddress(address: MemoryAddress, layout: MemoryLayout): MemorySegment? {
            if (address == 0L) return null
            return MemorySegment(
                BufferHolder.Root(
                    JNI.getByteBufferFromPointer(address, layout.size.toInt())!!
                )
            )
        }
    }
}
