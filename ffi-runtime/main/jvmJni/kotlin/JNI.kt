package dev.whyoleg.ffi

import java.nio.*

public object JNI {
    init {
        LibraryLoader.loadFromResources("/libs/macos-arm64/libffi-jni.dylib", "dylib")
        LibraryLoader.init()
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
