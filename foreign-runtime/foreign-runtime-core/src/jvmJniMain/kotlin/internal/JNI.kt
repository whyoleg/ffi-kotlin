package dev.whyoleg.foreign.internal

import dev.whyoleg.foreign.library.*
import java.nio.*

internal object JNI {
    init {
        EmbeddedLibraryLoader.Current.loadLibrary("foreign-core-jni")
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
