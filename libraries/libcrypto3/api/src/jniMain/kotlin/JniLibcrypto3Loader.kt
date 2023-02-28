package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

internal class JniLibcrypto3Loader : LibraryLoader {
    override val key: String get() = "crypto-ffi-jni"
    override val dependencies: List<String> get() = listOf("crypto")
    override fun load(): Unit = EmbeddedLibraryLoader.Current.loadLibrary("crypto-ffi-jni")
}
