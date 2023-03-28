package dev.whyoleg.foreign.memory

import java.nio.*

internal object JNI {
    @JvmStatic
    @JvmName("getByteBufferFromPointer")
    internal external fun getByteBufferFromPointer(pointer: Long, size: Int): ByteBuffer?

    @JvmStatic
    @JvmName("getPointerFromByteBuffer")
    internal external fun getPointerFromByteBuffer(buffer: ByteBuffer?): Long

    @JvmStatic
    @JvmName("getStringFromPointer")
    internal external fun getStringFromPointer(pointer: Long): String?
}