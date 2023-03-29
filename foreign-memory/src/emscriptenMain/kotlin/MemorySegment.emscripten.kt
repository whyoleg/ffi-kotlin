package dev.whyoleg.foreign.memory

//@ForeignMemoryApi
//public inline fun foreignMemory(block: (address: MemoryAddress) -> Unit): MemorySegment {
//    val segment = MemoryObject.Default.autoScope.allocateMemory(OSSL_PARAM.layout)
//    ffi_OSSL_PARAM_construct_utf8_string(key.address, buf.address, bsize.toInt(), segment.address)
//    return OSSL_PARAM.accessor.wrap(segment)
//}

@ForeignMemoryApi
public actual class MemorySegment internal constructor(
    public actual val address: MemoryAddress,
    public actual val size: MemoryAddressSize,
    private val memory: WasmMemory,
    private val cleaner: Cleaner?
) {
    private var _isAccessible = true
    public actual val isAccessible: Boolean get() = _isAccessible

    internal fun makeInaccessible() {
        _isAccessible = false
    }

    //TODO: copy check access and offset from native
    public actual fun loadByte(offset: MemoryAddressSize): Byte = memory.loadByte(address + offset)
    public actual fun storeByte(offset: MemoryAddressSize, value: Byte): Unit = memory.storeByte(address + offset, value)

    public actual fun loadInt(offset: MemoryAddressSize): Int = memory.loadInt(address + offset)
    public actual fun storeInt(offset: MemoryAddressSize, value: Int): Unit = memory.storeInt(address + offset, value)

    public actual fun loadLong(offset: MemoryAddressSize): Long = TODO("proper long support")
    public actual fun storeLong(offset: MemoryAddressSize, value: Long): Unit = TODO("proper long support")

    public actual fun loadString(offset: MemoryAddressSize): String {
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

    public actual fun storeString(offset: MemoryAddressSize, value: String) {
        val bytes = value.encodeToByteArray()
        storeByteArray(offset, bytes)
        storeByte(offset + bytes.size, 0)
    }

    public actual fun loadByteArray(offset: MemoryAddressSize, array: ByteArray, arrayStartIndex: Int, arrayEndIndex: Int) {
        (arrayStartIndex until arrayEndIndex).forEachIndexed { arrayIndex, index ->
            array[arrayIndex] = loadByte(offset + index)
        }
    }

    public actual fun storeByteArray(offset: MemoryAddressSize, array: ByteArray, arrayStartIndex: Int, arrayEndIndex: Int) {
        (arrayStartIndex until arrayEndIndex).forEachIndexed { arrayIndex, index ->
            storeByte(offset + index, array[arrayIndex])
        }
    }

    public actual fun loadPointed(offset: MemoryAddressSize, pointedLayout: MemoryLayout): MemorySegment? {
        //TODO: is it possible to support storing pointers to other memory?
        val address = loadInt(offset)
        if (address == 0) return null
        return MemorySegment(address, pointedLayout.size, memory, null)
    }

    public actual fun storePointed(offset: MemoryAddressSize, pointedLayout: MemoryLayout, value: MemorySegment?) {
        val address = if (value != null) {
            check(memory === value.memory) { "WASM cross memory access is not supported" }
            value.address
        } else 0

        storeInt(offset, address)
    }

    public actual fun loadSegment(offset: MemoryAddressSize, valueLayout: MemoryLayout): MemorySegment {
        return MemorySegment(address + offset, valueLayout.size, memory, cleaner)
    }

    public actual fun storeSegment(offset: MemoryAddressSize, valueLayout: MemoryLayout, value: MemorySegment) {
        check(memory === value.memory) { "WASM cross memory access is not supported" }

        //TODO: optimize?
        repeat(valueLayout.size) { i ->
            storeByte(offset + i, value.loadByte(i))
        }
    }

    public actual companion object {
        public actual val Empty: MemorySegment = MemorySegment(0, 0, WasmMemory.Empty, null)
    }
}
