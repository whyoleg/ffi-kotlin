package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.library.*

internal class JniLibcrypto3Loader : LibraryLoader {
    override val key: String get() = "foreign-crypto-jni"
    override val dependencies: List<String> get() = listOf("crypto")
    override fun load(): Unit = EmbeddedLibraryLoader.Current.loadLibrary(key)
}
