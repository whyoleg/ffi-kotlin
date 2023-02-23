package dev.whyoleg.ffi

import java.io.*
import java.nio.*

//TODO: DROP ByteBuffers and use unsafe!

internal const val pointerSize = 8 //64 bits

//public only for interop - hide it somehow

@JvmInline
public value class NativePointer(
    public val value: Long,
) {
    public companion object {
        public val NULL: NativePointer = NativePointer(0)
    }
}

public fun NativeMemory(pointer: NativePointer, size: Int): NativeMemory? {
    if (pointer == NativePointer.NULL) return null
    return NativeMemory(JNI.getByteBufferFromPointer(pointer.value, size) ?: return null)
}

@JvmInline
public value class NativeMemory
internal constructor(
    private val buffer: ByteBuffer,
) {
    public val pointer: NativePointer get() = NativePointer(JNI.getPointerFromByteBuffer(buffer))
    public val size: Int get() = buffer.capacity()

    public fun asReadOnly(): NativeMemory = NativeMemory(buffer.asReadOnlyBuffer())

    public fun loadByte(index: Int): Byte = buffer.get(index)
    public fun storeByte(index: Int, value: Byte): Unit = buffer.run { put(index, value) }
    public fun loadInt(index: Int): Int = buffer.getInt(index)
    public fun storeInt(index: Int, value: Int): Unit = buffer.run { putInt(index, value) }
    public fun loadLong(index: Int): Long = buffer.getLong(index)
    public fun storeLong(index: Int, value: Long): Unit = buffer.run { putLong(index, value) }

    public fun loadByteArray(
        index: Int,
        value: ByteArray,
        valueOffset: Int = 0,
        valueSize: Int = value.size,
    ): Unit = buffer.run {
        position(index)
        buffer.get(value, valueOffset, valueSize)
    }

    public fun storeByteArray(
        index: Int,
        value: ByteArray,
        valueOffset: Int = 0,
        valueSize: Int = value.size,
    ): Unit = buffer.run {
        position(index)
        buffer.put(value, valueOffset, valueSize)
    }

    public fun copyTo(destination: NativeMemory) {
        destination.buffer.position(0)
        buffer.position(0)
        destination.buffer.put(buffer)
    }

    public fun copyTo(
        destination: NativeMemory,
        destinationOffset: Int,
        destinationSize: Int,
    ) {
        buffer.position(0)
        destination.buffer.position(destinationOffset)
        destination.buffer.limit(destinationOffset + destinationSize)
        destination.buffer.put(buffer)
    }
}

internal abstract class NativeAllocator : Closeable {
    abstract fun allocate(size: Int): NativeMemory

    object Default : NativeAllocator() {
        override fun allocate(size: Int): NativeMemory = NativeMemory(ByteBuffer.allocateDirect(size))

        override fun close() {
        }
    }
}
