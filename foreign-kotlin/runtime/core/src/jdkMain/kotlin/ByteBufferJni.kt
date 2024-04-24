package dev.whyoleg.foreign

import java.nio.*

//TODO: looks like this can be also avoided by using reflection access (will be still faster then JNI)
internal object ByteBufferJni {
    init {
        LibraryLoader.loadEmbedded("foreign-runtime-core-jni", false)
    }

    @JvmStatic
    @JvmName("getByteBufferFromPointer")
    internal external fun getByteBufferFromPointer(pointer: Long, size: Long): ByteBuffer?

    @JvmStatic
    @JvmName("getPointerFromByteBuffer")
    internal external fun getPointerFromByteBuffer(buffer: ByteBuffer?): Long

    @JvmStatic
    @JvmName("getStringFromPointer")
    internal external fun getStringFromPointer(pointer: Long): String?
}
