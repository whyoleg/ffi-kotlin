package dev.whyoleg.ffi.c

import kotlinx.cinterop.*
import kotlinx.cinterop.CPointed
import kotlin.native.internal.NativePtr

public actual data class NativePointer internal constructor(internal val value: NativePtr) {
    public actual companion object {
        public actual val NULL: NativePointer = NativePointer(NativePtr.NULL)
    }
}

public actual class NativeLayout private constructor(internal val byteSize: Int, internal val align: Int = byteSize) {
    public actual companion object {
        public actual val Empty: NativeLayout = NativeLayout(0)
        public actual val Byte: NativeLayout = NativeLayout(kotlin.Byte.SIZE_BYTES)
        public actual val Int: NativeLayout = NativeLayout(kotlin.Int.SIZE_BYTES)
        public actual val Long: NativeLayout = NativeLayout(kotlin.Long.SIZE_BYTES)
        public actual val Pointer: NativeLayout = NativeLayout(kotlin.Int.SIZE_BYTES)
//        public fun of(byteSize: Int): NativeLayout = NativeLayout(byteSize)
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

    private fun <T : NativePointed> interpret(offset: Int): T = interpretNullablePointed(pointer.value + offset.toLong())!!
    private fun <T : CPointed> interpretPointer(offset: Int): kotlinx.cinterop.CPointer<T>? =
        interpretCPointer(pointer.value + offset.toLong())!!

    public actual fun loadByte(offset: Int): Byte = interpret<ByteVar>(offset).value
    public actual fun storeByte(offset: Int, value: Byte): Unit = run { interpret<ByteVar>(offset).value = value }
    public actual fun loadInt(offset: Int): Int = interpret<IntVar>(offset).value
    public actual fun storeInt(offset: Int, value: Int): Unit = run { interpret<IntVar>(offset).value = value }
    public actual fun loadLong(offset: Int): Long = interpret<LongVar>(offset).value
    public actual fun storeLong(offset: Int, value: Long): Unit = run { interpret<LongVar>(offset).value = value }

    public actual fun loadPointer(offset: Int): NativePointer = NativePointer(interpret<CPointerVar<*>>(offset).value.rawValue)

    public actual fun storePointer(offset: Int, pointer: NativePointer): Unit =
        run { interpret<CPointerVar<CPointed>>(offset).value = interpretCPointer(pointer.value) }

    public actual fun loadString(offset: Int): String = interpretPointer<ByteVar>(offset)!!.toKString()

    public actual fun storeString(offset: Int, value: String) {
        val bytes = value.encodeToByteArray()
        storeByteArray(offset, bytes)
        storeByte(offset + bytes.size, 0)
    }


    public actual fun loadByteArray(offset: Int, array: ByteArray, arrayOffset: Int, arraySize: Int) {
        interpretPointer<ByteVar>(offset)!!.readBytes(arraySize - arrayOffset).copyInto(array, arrayOffset)
    }

    //TODO: optimize
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
internal actual fun createDefaultNativeAllocator(): NativeAllocator = DefaultAllocator(Arena())

private class DefaultAllocator(
    private val arena: Arena,
) : NativeAllocator() {
    private fun alloc(size: Int, align: Int): NativeMemory = NativeMemory(NativePointer(arena.alloc(size, align).rawPtr), size)

    override fun allocate(layout: NativeLayout): NativeMemory = alloc(layout.byteSize, layout.align)
    override fun allocateArray(layout: NativeLayout, size: Int): NativeMemory = alloc(layout.byteSize * size, layout.align)

    override fun allocateString(value: String): NativeMemory {
        val bytes = value.encodeToByteArray()
        return alloc(bytes.size + 1, 1).also {
            it.storeByteArray(0, bytes)
            it.storeByte(bytes.size, 0)
        }
    }

    override fun close() {
        arena.clear()
    }
}
