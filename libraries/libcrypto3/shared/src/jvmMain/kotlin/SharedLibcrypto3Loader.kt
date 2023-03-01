package dev.whyoleg.ffi.libcrypto3.shared

import dev.whyoleg.ffi.c.*

internal class SharedLibcrypto3Loader : LibraryLoader {
    override val key: String get() = "crypto"
    override val dependencies: List<String> get() = emptyList()
    override fun load(): Unit = System.loadLibrary("crypto")
}
