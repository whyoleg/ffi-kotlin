package dev.whyoleg.ffi.c

import java.nio.*

//TODO: rename to FFI?
public object JNI {
    init {
        EmbeddedLibraryLoader.Current.loadLibrary("ffi-jni")
        loadLibraries()
    }

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
