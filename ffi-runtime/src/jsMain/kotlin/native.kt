package dev.whyoleg.ffi

import org.khronos.webgl.*

internal const val pointerSize = 4 //32 bits

//public only for interop - hide it somehow

public class NativePointer(
    public val value: Int,
) {
    public companion object {
        public val NULL: NativePointer = NativePointer(0)
    }
}

public fun NativeMemory(pointer: NativePointer, size: Int): NativeMemory? {
    if (pointer == NativePointer.NULL) return null
    return NativeMemory(size, pointer)
}

public class NativeMemory
internal constructor(
    public val size: Int,
    public val pointer: NativePointer,
) {
    public fun asReadOnly(): NativeMemory = this

    public fun loadByte(index: Int): Byte = FFI.HEAPU8[pointer.value + index]
    public fun storeByte(index: Int, value: Byte): Unit = run { FFI.HEAPU8[pointer.value + index] = value }
    public fun loadInt(index: Int): Int = FFI.HEAPU32[(pointer.value + index) / Int.SIZE_BYTES]
    public fun storeInt(index: Int, value: Int): Unit = run { FFI.HEAPU32[(pointer.value + index) / Int.SIZE_BYTES] = value }

    //TODO: proper long support
    public fun loadLong(index: Int): Long = loadInt(index).toLong()
    public fun storeLong(index: Int, value: Long): Unit = storeInt(index, value.toInt())

    public fun loadByteArray(
        index: Int,
        value: ByteArray,
        valueOffset: Int = 0,
        valueSize: Int = value.size,
    ) {
        repeat(valueSize) { i ->
            value[valueOffset + i] = loadByte(index + i)
        }
    }

    public fun storeByteArray(
        index: Int,
        value: ByteArray,
        valueOffset: Int = 0,
        valueSize: Int = value.size,
    ) {
        repeat(valueSize) { i ->
            storeByte(index + i, value[valueOffset + i])
        }
    }

    public fun copyTo(destination: NativeMemory) {
        repeat(size) { i ->
            destination.storeByte(i, loadByte(i))
        }
    }

    public fun copyTo(
        destination: NativeMemory,
        destinationOffset: Int,
        destinationSize: Int,
    ) {
        repeat(destinationSize) { i ->
            destination.storeByte(destinationOffset + i, loadByte(i))
        }
    }
}

@PublishedApi
internal abstract class NativeAllocator {
    abstract fun allocate(size: Int): NativeMemory

    abstract fun close()

    class Default : NativeAllocator() {
        private val pointers = ArrayDeque<Int>()
        override fun allocate(size: Int): NativeMemory {
            val pointer = FFI.asm.malloc(size)
            pointers.addLast(pointer)
            return NativeMemory(size, NativePointer(pointer))
        }

        override fun close() {
            while (true) FFI.asm.free(pointers.removeFirstOrNull() ?: return)
        }
    }

    //TODO
    object Auto : NativeAllocator() {
        override fun allocate(size: Int): NativeMemory {
            val pointer = FFI.asm.malloc(size)
            return NativeMemory(size, NativePointer(pointer))
        }

        override fun close() {

        }
    }
}

//TODO how to do it for work with everything???
@JsModule("ffi-libcrypto")
@JsNonModule
@JsName("Module")
private external object FFI {
    val HEAPU8: Uint8Array
    val HEAPU16: Uint16Array
    val HEAPU32: Uint32Array

    object asm {
        fun malloc(size: Int): Int
        fun free(pointer: Int)
    }
}
