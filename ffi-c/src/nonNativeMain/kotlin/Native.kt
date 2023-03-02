package dev.whyoleg.ffi.c

//TODO: should `offset` be Int or some PlatformInt?

public expect class NativePointer {
    public companion object {
        public val NULL: NativePointer
    }
}

public expect class NativeLayout {
    public companion object {
        public val Empty: NativeLayout
        public val Byte: NativeLayout
        public val Int: NativeLayout
        public val Long: NativeLayout
        public val Pointer: NativeLayout
    }
}

internal expect fun NativeMemory(
    pointer: NativePointer,
    layout: NativeLayout,
): NativeMemory

public expect class NativeMemory {
    public val pointer: NativePointer //TODO!!!
    public val size: Int
    public fun asReadOnly(): NativeMemory

    public fun loadByte(offset: Int): Byte
    public fun storeByte(offset: Int, value: Byte)

    public fun loadInt(offset: Int): Int
    public fun storeInt(offset: Int, value: Int)

    public fun loadLong(offset: Int): Long
    public fun storeLong(offset: Int, value: Long)

    public fun loadPointer(offset: Int): NativePointer
    public fun storePointer(offset: Int, pointer: NativePointer)

    public fun loadString(offset: Int): String
    public fun storeString(offset: Int, value: String)

    public fun loadByteArray(offset: Int, array: ByteArray, arrayOffset: Int = 0, arraySize: Int = array.size)
    public fun storeByteArray(offset: Int, array: ByteArray, arrayOffset: Int = 0, arraySize: Int = array.size)

//    public fun copyTo(offset: Int, destination: NativeMemory)
//    public fun copyTo(
//        offset: PointerInt,
//        destination: NativeMemory,
//        destinationOffset: PointerInt = PointerInt.Zero,
//        destinationSize: PointerInt = destination.size,
//    )

    public fun copyElementTo(
        elementIndex: Int,
        elementLayout: NativeLayout,
        offset: Int,
        destination: NativeMemory,
        destinationOffset: Int = 0,
    )
}

//TODO: RawAllocator with bytes
public abstract class NativeAllocator : AutoCloseable {
    public abstract fun allocate(layout: NativeLayout): NativeMemory
    public abstract fun allocateArray(layout: NativeLayout, size: Int): NativeMemory
    public abstract fun allocateString(value: String): NativeMemory
}

@PublishedApi
internal expect fun createDefaultNativeAllocator(): NativeAllocator
