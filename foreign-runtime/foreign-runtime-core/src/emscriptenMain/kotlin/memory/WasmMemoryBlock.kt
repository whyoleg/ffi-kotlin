package dev.whyoleg.foreign.memory

import dev.whyoleg.foreign.internal.*

@ForeignMemoryApi
internal class WasmMemoryBlock(
    override val address: MemoryAddressSize,
    override val size: MemoryAddressSize,
    private val memory: WasmMemory,
    private val cleaner: Cleaner?
) : MemoryBlock() {

    private var _isAccessible = true
    override val isAccessible: Boolean get() = _isAccessible

    internal fun makeInaccessible() {
        _isAccessible = false
    }


    //TODO: copy check access and offset from native
    override fun loadByte(offset: MemoryAddressSize): Byte = memory.loadByte(address + offset)
    override fun storeByte(offset: MemoryAddressSize, value: Byte): Unit = memory.storeByte(address + offset, value)

    override fun loadInt(offset: MemoryAddressSize): Int = memory.loadInt(address + offset)
    override fun storeInt(offset: MemoryAddressSize, value: Int): Unit = memory.storeInt(address + offset, value)

    override fun loadLong(offset: MemoryAddressSize): Long = TODO("proper long support")
    override fun storeLong(offset: MemoryAddressSize, value: Long): Unit = TODO("proper long support")

    override fun loadString(offset: MemoryAddressSize, unsafe: Boolean): String {
        //TODO: check for unsafe flag based on size
        //TODO: optimize by pre-allocating ByteArray and resizing it to not read bytes 2 times
        var length = 0
        while (true) {
            val b = loadByte(offset + length)
            if (b == 0.toByte()) break
            length++
        }
        val array = ByteArray(length)
        loadByteArray(offset, array)
        return array.decodeToString()
    }

    override fun storeString(offset: MemoryAddressSize, value: String) {
        val bytes = value.encodeToByteArray()
        storeByteArray(offset, bytes)
        storeByte(offset + bytes.size, 0)
    }

    override fun loadByteArray(offset: MemoryAddressSize, array: ByteArray, arrayStartIndex: Int, arrayEndIndex: Int) {
        (arrayStartIndex until arrayEndIndex).forEachIndexed { arrayIndex, index ->
            array[arrayIndex] = loadByte(offset + index)
        }
    }

    override fun storeByteArray(offset: MemoryAddressSize, array: ByteArray, arrayStartIndex: Int, arrayEndIndex: Int) {
        (arrayStartIndex until arrayEndIndex).forEachIndexed { arrayIndex, index ->
            storeByte(offset + index, array[arrayIndex])
        }
    }

    override fun loadPointed(offset: MemoryAddressSize, pointedLayout: MemoryBlockLayout): MemoryBlock? {
        //TODO: is it possible to support storing pointers to other memory?
        return fromAddress(loadInt(offset), pointedLayout, memory)
    }

    override fun storePointed(offset: MemoryAddressSize, pointedLayout: MemoryBlockLayout, value: MemoryBlock?) {
        val address = if (value != null) {
            check(memory === (value as WasmMemoryBlock).memory) { "WASM cross memory access is not supported" }
            value.address
        } else 0

        storeInt(offset, address)
    }

    override fun loadSegment(offset: MemoryAddressSize, valueLayout: MemoryBlockLayout): MemoryBlock {
        return WasmMemoryBlock(address + offset, valueLayout.size, memory, cleaner)
    }

    override fun storeSegment(offset: MemoryAddressSize, valueLayout: MemoryBlockLayout, value: MemoryBlock) {
        check(memory === (value as WasmMemoryBlock).memory) { "WASM cross memory access is not supported" }

        //TODO: optimize?
        repeat(valueLayout.size) { i ->
            storeByte(offset + i, value.loadByte(i))
        }
    }

    private fun resize(size: MemoryAddressSize): MemoryBlock {
        return WasmMemoryBlock(address, size, memory, cleaner)
    }

    override fun resize(layout: MemoryBlockLayout): MemoryBlock = resize(layout.size)
    override fun resize(elementLayout: MemoryBlockLayout, elementsCount: Int): MemoryBlock = resize(elementLayout.size * elementsCount)

    companion object {
        internal val NULL
            get() = WasmMemoryBlock(
                address = memoryAddressSizeZero(),
                size = memoryAddressSizeZero(),
                memory = WasmMemory.Empty,
                cleaner = null
            )

        internal fun fromAddress(address: MemoryAddressSize, layout: MemoryBlockLayout, memory: WasmMemory): MemoryBlock? {
            if (address == 0) return null
            return WasmMemoryBlock(address, layout.size, memory, null)
        }
    }
}
