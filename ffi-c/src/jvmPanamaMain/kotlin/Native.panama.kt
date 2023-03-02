package dev.whyoleg.ffi.c

import java.lang.foreign.*

public actual class NativePointer internal constructor(internal val segment: MemorySegment) {
    public actual companion object {
        public actual val NULL: NativePointer = NativePointer(MemorySegment.NULL)
    }
}

//TODO: null size
public actual class NativeLayout private constructor(internal val value: MemoryLayout?) {
    public actual companion object {
        public actual val Empty: NativeLayout = NativeLayout(null)
        public actual val Byte: NativeLayout = NativeLayout(ValueLayout.JAVA_BYTE)
        public actual val Int: NativeLayout = NativeLayout(ValueLayout.JAVA_INT)
        public actual val Long: NativeLayout = NativeLayout(ValueLayout.JAVA_LONG)
        public actual val Pointer: NativeLayout = NativeLayout(ValueLayout.ADDRESS)
        public fun of(layout: GroupLayout): NativeLayout = NativeLayout(layout)
    }
}

internal actual fun NativeMemory(
    pointer: NativePointer,
    layout: NativeLayout,
): NativeMemory = NativeMemory(pointer.segment) //TODO size?

public actual class NativeMemory internal constructor(private val segment: MemorySegment) {

    public actual val pointer: NativePointer get() = NativePointer(segment) //TODO?
    public actual val size: Int get() = segment.byteSize().toInt()

    public actual fun asReadOnly(): NativeMemory = NativeMemory(segment.asReadOnly())

    public actual fun loadByte(offset: Int): Byte = segment.get(ValueLayout.JAVA_BYTE, offset.toLong())
    public actual fun storeByte(offset: Int, value: Byte): Unit = segment.set(ValueLayout.JAVA_BYTE, offset.toLong(), value)

    public actual fun loadInt(offset: Int): Int = segment.get(ValueLayout.JAVA_INT, offset.toLong())
    public actual fun storeInt(offset: Int, value: Int): Unit = segment.set(ValueLayout.JAVA_INT, offset.toLong(), value)

    public actual fun loadLong(offset: Int): Long = segment.get(ValueLayout.JAVA_LONG, offset.toLong())
    public actual fun storeLong(offset: Int, value: Long): Unit = segment.set(ValueLayout.JAVA_LONG, offset.toLong(), value)

    public actual fun loadPointer(offset: Int): NativePointer = NativePointer(segment.get(ValueLayout.ADDRESS, offset.toLong()))
    public actual fun storePointer(offset: Int, pointer: NativePointer): Unit =
        segment.set(ValueLayout.ADDRESS, offset.toLong(), pointer.segment)

    public actual fun loadString(offset: Int): String = segment.getUtf8String(offset.toLong())
    public actual fun storeString(offset: Int, value: String): Unit = segment.setUtf8String(offset.toLong(), value)

    public actual fun loadByteArray(offset: Int, array: ByteArray, arrayOffset: Int, arraySize: Int) {
        MemorySegment.copy(
            /*src*/ segment, ValueLayout.JAVA_BYTE, offset.toLong(),
            /*dst*/ array, arrayOffset,
            arraySize
        )
    }

    public actual fun storeByteArray(offset: Int, array: ByteArray, arrayOffset: Int, arraySize: Int) {
        MemorySegment.copy(
            /*src*/ array, arrayOffset,
            /*dst*/ segment, ValueLayout.JAVA_BYTE, offset.toLong(),
            arraySize
        )
    }

    public actual fun copyElementTo(
        elementIndex: Int,
        elementLayout: NativeLayout,
        offset: Int,
        destination: NativeMemory,
        destinationOffset: Int,
    ) {
        val elementSize = elementLayout.value?.byteSize() ?: 0L
        MemorySegment.copy(
            segment, offset.toLong(),
            destination.segment, destinationOffset + elementIndex * elementSize,
            elementSize
        )
    }
}

@PublishedApi
internal actual fun createDefaultNativeAllocator(): NativeAllocator = DefaultAllocator(Arena.openConfined())

private class DefaultAllocator(
    private val arena: Arena,
) : NativeAllocator() {
    override fun allocate(layout: NativeLayout): NativeMemory = NativeMemory(arena.allocate(layout.value!!)) //TODO
    override fun allocateArray(layout: NativeLayout, size: Int): NativeMemory =
        NativeMemory(arena.allocateArray(layout.value!!, size.toLong()))

    override fun allocateString(value: String): NativeMemory = NativeMemory(arena.allocateUtf8String(value))
    override fun close(): Unit = arena.close()
}
