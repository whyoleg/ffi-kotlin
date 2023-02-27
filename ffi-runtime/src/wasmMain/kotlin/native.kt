package dev.whyoleg.ffi

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

    public fun loadByte(index: Int): Byte = getU8(pointer.value + index)
    public fun storeByte(index: Int, value: Byte): Unit = setU8(pointer.value + index, value)
    public fun loadInt(index: Int): Int = getU32((pointer.value + index) / Int.SIZE_BYTES)
    public fun storeInt(index: Int, value: Int): Unit = setU32((pointer.value + index) / Int.SIZE_BYTES, value)

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
            val pointer = malloc(size)
            pointers.addLast(pointer)
            return NativeMemory(size, NativePointer(pointer))
        }

        override fun close() {
            while (true) free(pointers.removeFirstOrNull() ?: return)
        }
    }

    //TODO
    object Auto : NativeAllocator() {
        override fun allocate(size: Int): NativeMemory {
            val pointer = malloc(size)
            return NativeMemory(size, NativePointer(pointer))
        }

        override fun close() {

        }
    }
}

//TODO: those are really super dependent - need to check on how to provide it better
@JsFun("(index) => imports.Module.HEAPU8[index]")
private external fun getU8(index: Int): Byte

@JsFun("(index, value) => imports.Module.HEAPU8[index] = value")
private external fun setU8(index: Int, value: Byte)

@JsFun("(index) => imports.Module.HEAPU16[index]")
private external fun getU16(index: Int): Short

@JsFun("(index) => imports.Module.HEAPU32[index]")
private external fun getU32(index: Int): Int

@JsFun("(index, value) => imports.Module.HEAPU32[index] = value")
private external fun setU32(index: Int, value: Int)

@JsFun("(size) => imports.Module.asm.malloc(size)")
private external fun malloc(size: Int): Int

@JsFun("(pointer) => imports.Module.asm.free(pointer)")
private external fun free(pointer: Int)
