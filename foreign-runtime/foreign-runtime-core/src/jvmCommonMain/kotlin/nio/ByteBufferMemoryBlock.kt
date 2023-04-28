package dev.whyoleg.foreign.nio

import dev.whyoleg.foreign.memory.*
import java.nio.*

@ForeignMemoryApi
internal class ByteBufferMemoryBlock(private val holder: BufferHolder) : MemoryBlock() {
    private inline val buffer: ByteBuffer get() = holder.access()

    override val address: MemoryAddressSize get() = ByteBufferJni.getPointerFromByteBuffer(buffer)
    override val size: MemoryAddressSize get() = buffer.capacity().toLong()
    override val isAccessible: Boolean get() = holder.isAccessible

    //TODO: copy check access and offset from native
    override fun loadByte(offset: MemoryAddressSize): Byte = buffer.get(offset.toInt())
    override fun storeByte(offset: MemoryAddressSize, value: Byte) {
        buffer.put(offset.toInt(), value)
    }

    override fun loadInt(offset: MemoryAddressSize): Int = buffer.getInt(offset.toInt())
    override fun storeInt(offset: MemoryAddressSize, value: Int) {
        buffer.putInt(offset.toInt(), value)
    }

    override fun loadLong(offset: MemoryAddressSize): Long = buffer.getLong(offset.toInt())
    override fun storeLong(offset: MemoryAddressSize, value: Long) {
        buffer.putLong(offset.toInt(), value)
    }

    override fun loadString(offset: MemoryAddressSize, unsafe: Boolean): String {
        //TODO: check buffer size if unsafe=false?
        return ByteBufferJni.getStringFromPointer(address + offset.toInt())!!
    }

    override fun storeString(offset: MemoryAddressSize, value: String) {
        val bytes = value.encodeToByteArray()
        storeByteArray(offset, bytes)
        storeByte(offset + bytes.size, 0)
    }

    override fun loadByteArray(offset: MemoryAddressSize, array: ByteArray, arrayStartIndex: Int, arrayEndIndex: Int) {
        buffer.clear()
        buffer.position(offset.toInt())
        //TODO: recheck indexes
        buffer.get(array, arrayStartIndex, arrayEndIndex - arrayStartIndex)
    }

    override fun storeByteArray(offset: MemoryAddressSize, array: ByteArray, arrayStartIndex: Int, arrayEndIndex: Int) {
        buffer.clear()
        buffer.position(offset.toInt())
        //TODO: recheck indexes
        buffer.put(array, arrayStartIndex, arrayEndIndex - arrayStartIndex)
    }

    override fun loadPointed(offset: MemoryAddressSize, pointedLayout: MemoryBlockLayout): MemoryBlock? {
        return fromAddress(loadLong(offset), pointedLayout.size)
    }

    override fun storePointed(offset: MemoryAddressSize, pointedLayout: MemoryBlockLayout, value: MemoryBlock?) {
        val address = value?.address ?: 0
        storeLong(offset, address)
    }

    override fun loadSegment(offset: MemoryAddressSize, valueLayout: MemoryBlockLayout): MemoryBlock {
        buffer.clear()
        buffer.position(offset.toInt())
        buffer.limit((offset + valueLayout.size).toInt())
        return ByteBufferMemoryBlock(holder.view(buffer.slice()))
    }

    override fun storeSegment(offset: MemoryAddressSize, valueLayout: MemoryBlockLayout, value: MemoryBlock) {
        buffer.clear()
        buffer.position(offset.toInt())
        buffer.limit((offset + valueLayout.size).toInt())
        value as ByteBufferMemoryBlock //TODO: fallback to store by byte?
        value.buffer.clear()
        buffer.put(value.buffer)
    }

    private fun resize(size: MemoryAddressSize): MemoryBlock {
        return ByteBufferMemoryBlock(holder.view(ByteBufferJni.getByteBufferFromPointer(address, size)!!))
    }

    override fun resize(layout: MemoryBlockLayout): MemoryBlock = resize(layout.size)
    override fun resize(elementLayout: MemoryBlockLayout, elementsCount: Int): MemoryBlock = resize(elementLayout.size * elementsCount)

    companion object {
        internal val NULL: MemoryBlock
            get() = ByteBufferMemoryBlock(BufferHolder.Root(ByteBufferJni.getByteBufferFromPointer(0, 0)!!))

        internal fun fromAddress(
            address: MemoryAddressSize,
            size: MemoryAddressSize,
        ): MemoryBlock? = fromAddress(address, size) {}

        internal inline fun fromAddress(
            address: MemoryAddressSize,
            size: MemoryAddressSize,
            onRoot: (root: BufferHolder.Root) -> Unit
        ): MemoryBlock? {
            if (address == 0L) return null
            val holder = BufferHolder.Root(ByteBufferJni.getByteBufferFromPointer(address, size)!!).also(onRoot)
            return ByteBufferMemoryBlock(holder)
        }
    }
}
