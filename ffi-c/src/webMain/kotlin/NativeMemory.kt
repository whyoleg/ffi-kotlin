package dev.whyoleg.ffi

internal const val pointerSize = 4 //32 bits

//public only for interop - hide it somehow

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

    public fun loadByte(index: Int): Byte = FFI.getByte(pointer.value + index)
    public fun storeByte(index: Int, value: Byte): Unit = FFI.setByte(pointer.value + index, value)
    public fun loadInt(index: Int): Int = FFI.getInt((pointer.value + index) / Int.SIZE_BYTES)
    public fun storeInt(index: Int, value: Int): Unit = FFI.setInt((pointer.value + index) / Int.SIZE_BYTES, value)

    //TODO: proper long support
    public fun loadLong(index: Int): Long = TODO("proper long support")
    public fun storeLong(index: Int, value: Long): Unit = TODO("proper long support")

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
