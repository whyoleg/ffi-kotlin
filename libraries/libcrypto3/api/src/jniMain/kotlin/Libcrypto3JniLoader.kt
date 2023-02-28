package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

internal class Libcrypto3JniLoader : LibraryLoader {
    override fun load() {
        EmbeddedLibraryLoader.Current.loadLibrary("crypto-ffi-jni")
    }
}
