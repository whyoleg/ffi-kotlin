package dev.whyoleg.ffi.c

import java.nio.*

public actual class NativePointer internal constructor(internal val value: Long) {
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
        public actual val Pointer: NativeLayout = NativeLayout(kotlin.Long.SIZE_BYTES)
        public fun of(byteSize: Int): NativeLayout = NativeLayout(byteSize)
    }
}

internal actual fun NativeMemory(
    pointer: NativePointer,
    layout: NativeLayout,
): NativeMemory = NativeMemory(JNI.getByteBufferFromPointer(pointer.value, layout.byteSize)!!)

public actual class NativeMemory internal constructor(private val buffer: ByteBuffer) {
    init {
        check(buffer.isDirect)
        buffer.order(ByteOrder.nativeOrder())
    }

    public actual val pointer: NativePointer get() = NativePointer(JNI.getPointerFromByteBuffer(buffer))
    public actual val size: Int get() = buffer.capacity()

    public actual fun asReadOnly(): NativeMemory = NativeMemory(buffer.asReadOnlyBuffer())

    public actual fun loadByte(offset: Int): Byte = buffer.get(offset)
    public actual fun storeByte(offset: Int, value: Byte): Unit = buffer.run { put(offset, value) }

    public actual fun loadInt(offset: Int): Int = buffer.getInt(offset)
    public actual fun storeInt(offset: Int, value: Int): Unit = buffer.run { putInt(offset, value) }

    public actual fun loadLong(offset: Int): Long = buffer.getLong(offset)
    public actual fun storeLong(offset: Int, value: Long): Unit = buffer.run { putLong(offset, value) }

    public actual fun loadPointer(offset: Int): NativePointer = NativePointer(loadLong(offset))
    public actual fun storePointer(offset: Int, pointer: NativePointer): Unit = storeLong(offset, pointer.value)

    public actual fun loadString(offset: Int): String = JNI.getStringFromPointer(pointer.value)!!
    public actual fun storeString(offset: Int, value: String) {
        val bytes = value.encodeToByteArray()
        storeByteArray(offset, bytes)
        storeByte(offset + bytes.size, 0)
    }

    public actual fun loadByteArray(offset: Int, array: ByteArray, arrayOffset: Int, arraySize: Int) {
        buffer.position(offset)
        buffer.get(array, arrayOffset, arraySize)
    }

    public actual fun storeByteArray(offset: Int, array: ByteArray, arrayOffset: Int, arraySize: Int) {
        buffer.position(offset)
        buffer.put(array, arrayOffset, arraySize)
    }

    public actual fun copyElementTo(
        elementIndex: Int,
        elementLayout: NativeLayout,
        offset: Int,
        destination: NativeMemory,
        destinationOffset: Int,
    ) {
        buffer.position(offset + elementIndex * elementLayout.byteSize)
        destination.buffer.position(destinationOffset + elementIndex * elementLayout.byteSize)
        destination.buffer.put(buffer)
    }
}

@PublishedApi
internal actual fun createDefaultNativeAllocator(): NativeAllocator = ByteBufferAllocator

private object ByteBufferAllocator : NativeAllocator() {
    private fun allocate(size: Int): NativeMemory = NativeMemory(ByteBuffer.allocateDirect(size))

    override fun allocate(layout: NativeLayout): NativeMemory = allocate(layout.byteSize)
    override fun allocateArray(layout: NativeLayout, size: Int): NativeMemory = allocate(layout.byteSize * size)
    override fun allocateString(value: String): NativeMemory {
        val bytes = value.encodeToByteArray()
        return allocate(bytes.size + 1).also {
            it.storeByteArray(0, bytes)
            it.storeByte(bytes.size, 0)
        }
    }

    override fun close() {}
}
