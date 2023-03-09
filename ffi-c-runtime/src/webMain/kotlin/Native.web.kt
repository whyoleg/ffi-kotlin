package dev.whyoleg.ffi.c

public actual class NativePointer internal constructor(internal val value: Int) {
    public actual companion object {
        public actual val NULL: NativePointer = NativePointer(0)
    }
}

public actual class NativeLayout private constructor(internal val byteSize: Int) {
    public actual companion object {
        public actual val Empty: NativeLayout = NativeLayout(0)
        public actual val Byte: NativeLayout = NativeLayout(kotlin.Byte.SIZE_BYTES)
        public actual val Int: NativeLayout = NativeLayout(kotlin.Int.SIZE_BYTES)
        public actual val Long: NativeLayout = NativeLayout(kotlin.Long.SIZE_BYTES)
        public actual val Pointer: NativeLayout = NativeLayout(kotlin.Int.SIZE_BYTES)
        public fun of(byteSize: Int): NativeLayout = NativeLayout(byteSize)
    }
}

internal actual fun NativeMemory(
    pointer: NativePointer,
    layout: NativeLayout,
): NativeMemory = NativeMemory(pointer, layout.byteSize)

public actual class NativeMemory internal constructor(
    public actual val pointer: NativePointer,
    public actual val size: Int,
) {
    public actual fun asReadOnly(): NativeMemory = this //TODO

    public actual fun loadByte(offset: Int): Byte = FFI.getByte(pointer.value + offset)
    public actual fun storeByte(offset: Int, value: Byte): Unit = FFI.setByte(pointer.value + offset, value)
    public actual fun loadInt(offset: Int): Int = FFI.getInt((pointer.value + offset) / Int.SIZE_BYTES)
    public actual fun storeInt(offset: Int, value: Int): Unit = FFI.setInt((pointer.value + offset) / Int.SIZE_BYTES, value)

    //TODO: proper long support (wasm is little-endian)
    public actual fun loadLong(offset: Int): Long = TODO("proper long support")
    public actual fun storeLong(offset: Int, value: Long): Unit = TODO("proper long support")

    public actual fun loadPointer(offset: Int): NativePointer = NativePointer(loadInt(offset))
    public actual fun storePointer(offset: Int, pointer: NativePointer): Unit = storeInt(offset, pointer.value)

    //TODO: optimize
    public actual fun loadString(offset: Int): String {
        var length = 0
        while (true) {
            val b = loadByte(length)
            if (b == 0.toByte()) break
            length++
        }
        return ByteArray(length, ::loadByte).decodeToString()
    }

    public actual fun storeString(offset: Int, value: String) {
        val bytes = value.encodeToByteArray()
        storeByteArray(offset, bytes)
        storeByte(offset + bytes.size, 0)
    }

    public actual fun loadByteArray(offset: Int, array: ByteArray, arrayOffset: Int, arraySize: Int) {
        repeat(arraySize) { i ->
            array[arrayOffset + i] = loadByte(offset + i)
        }
    }

    public actual fun storeByteArray(offset: Int, array: ByteArray, arrayOffset: Int, arraySize: Int) {
        repeat(arraySize) { i ->
            storeByte(offset + i, array[arrayOffset + i])
        }
    }

    public actual fun copyElementTo(
        elementIndex: Int,
        elementLayout: NativeLayout,
        offset: Int,
        destination: NativeMemory,
        destinationOffset: Int,
    ) {
        val elementOffset = elementIndex * elementLayout.byteSize
        repeat(elementLayout.byteSize) { i ->
            destination.storeByte(destinationOffset + elementOffset + i, loadByte(offset + elementOffset + i))
        }
    }
}

@PublishedApi
internal actual fun createDefaultNativeAllocator(): NativeAllocator = DefaultAllocator()

private class DefaultAllocator : MallocAllocator() {
    private val pointers = ArrayDeque<NativePointer>()
    override fun malloc(size: Int): NativeMemory = super.malloc(size).also {
        pointers.addLast(it.pointer)
    }

    override fun close() {
        while (true) free(pointers.removeFirstOrNull() ?: return)
    }
}

private abstract class MallocAllocator : NativeAllocator() {
    protected open fun malloc(size: Int): NativeMemory = NativeMemory(NativePointer(FFI.malloc(size)), size)
    protected fun free(pointer: NativePointer): Unit = FFI.free(pointer.value)

    override fun allocate(layout: NativeLayout): NativeMemory = malloc(layout.byteSize)
    override fun allocateArray(layout: NativeLayout, size: Int): NativeMemory = malloc(layout.byteSize * size)
    override fun allocateString(value: String): NativeMemory {
        val bytes = value.encodeToByteArray()
        return malloc(bytes.size + 1).also {
            it.storeByteArray(0, bytes)
            it.storeByte(bytes.size, 0)
        }
    }
}
