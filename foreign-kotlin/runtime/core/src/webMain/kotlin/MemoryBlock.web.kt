package dev.whyoleg.foreign

internal class WebAssemblyMemoryBlock(
    private val memory: WebAssemblyMemory,
    override val address: MemorySizeInt,
    override val size: MemorySizeInt,
//    private val cleaner: Cleaner?
) : InternalMemoryBlock {

    private var _isAccessible = true
    override val isAccessible: Boolean get() = _isAccessible

    internal fun release() {
        if (!_isAccessible) return
        _isAccessible = false
        memory.free(address)
    }

    private fun address(offset: MemorySizeInt, bytes: Int): MemorySizeInt {
        check(isAccessible) { "Not accessible" }
        check(offset + bytes <= size) { "Out of bound access [offset=$offset, bytes=$bytes, size=$size]" }

        return address + offset
    }

    override fun getByte(offset: MemorySizeInt): Byte {
        return memory.getByte(address(offset, Byte.SIZE_BYTES))
    }

    override fun setByte(offset: MemorySizeInt, value: Byte) {
        memory.setByte(address(offset, Byte.SIZE_BYTES), value)
    }

    override fun getInt(offset: MemorySizeInt): Int {
        return memory.getInt(address(offset, Int.SIZE_BYTES))
    }

    override fun setInt(offset: MemorySizeInt, value: Int) {
        memory.setInt(address(offset, Int.SIZE_BYTES), value)
    }

    override fun getLong(offset: MemorySizeInt): Long = TODO("proper long support")
    override fun setLong(offset: MemorySizeInt, value: Long): Unit = TODO("proper long support")

//    override fun getString(offset: MemorySizeInt, unsafe: Boolean): String {
//        //TODO: check for unsafe flag based on size
//        //TODO: optimize by pre-allocating ByteArray and resizing it to not read bytes 2 times
//        var length = 0
//        while (true) {
//            val b = getByte(offset + length)
//            if (b == 0.toByte()) break
//            length++
//        }
//        val array = ByteArray(length)
//        getByteArray(offset, array)
//        return array.decodeToString()
//    }
//
//    override fun setString(offset: MemorySizeInt, value: String) {
//        val bytes = value.encodeToByteArray()
//        setByteArray(offset, bytes)
//        setByte(offset + bytes.size, 0)
//    }
//
//    override fun getByteArray(offset: MemorySizeInt, array: ByteArray, arrayStartIndex: Int, arrayEndIndex: Int) {
//        (arrayStartIndex until arrayEndIndex).forEachIndexed { arrayIndex, index ->
//            array[arrayIndex] = getByte(offset + index)
//        }
//    }
//
//    override fun setByteArray(offset: MemorySizeInt, array: ByteArray, arrayStartIndex: Int, arrayEndIndex: Int) {
//        (arrayStartIndex until arrayEndIndex).forEachIndexed { arrayIndex, index ->
//            setByte(offset + index, array[arrayIndex])
//        }
//    }
//
//    override fun getPointed(offset: MemorySizeInt, pointedLayout: MemoryBlockLayout): MemoryBlock? {
//        //TODO: is it possible to support storing pointers to other memory?
//        return fromAddress(getInt(offset), pointedLayout, memory)
//    }
//
//    override fun setPointed(offset: MemorySizeInt, pointedLayout: MemoryBlockLayout, value: MemoryBlock?) {
//        val address = if (value != null) {
//            check(memory === (value as WasmMemoryBlock).memory) { "WASM cross memory access is not supported" }
//            value.address
//        } else 0
//
//        setInt(offset, address)
//    }
//
//    override fun getSegment(offset: MemorySizeInt, valueLayout: MemoryBlockLayout): MemoryBlock {
//        return WasmMemoryBlock(address + offset, valueLayout.size, memory, cleaner)
//    }
//
//    override fun setSegment(offset: MemorySizeInt, valueLayout: MemoryBlockLayout, value: MemoryBlock) {
//        check(memory === (value as WasmMemoryBlock).memory) { "WASM cross memory access is not supported" }
//
//        //TODO: optimize?
//        repeat(valueLayout.size) { i ->
//            setByte(offset + i, value.getByte(i))
//        }
//    }
//
//    private fun resize(size: MemorySizeInt): MemoryBlock {
//        return WasmMemoryBlock(address, size, memory, cleaner)
//    }
//
//    override fun resize(layout: MemoryBlockLayout): MemoryBlock = resize(layout.size)
//    override fun resize(elementLayout: MemoryBlockLayout, elementsCount: Int): MemoryBlock = resize(elementLayout.size * elementsCount)
//
//    companion object {
//        internal val NULL
//            get() = WasmMemoryBlock(
//                address = MemorySizeIntZero(),
//                size = MemorySizeIntZero(),
//                memory = WasmMemory.Empty,
//                cleaner = null
//            )
//
//        internal fun fromAddress(address: MemorySizeInt, layout: MemoryBlockLayout, memory: WasmMemory): MemoryBlock? {
//            if (address == 0) return null
//            return WasmMemoryBlock(address, layout.size, memory, null)
//        }
//    }
}
