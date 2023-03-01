package dev.whyoleg.ffi.libcrypto3.prebuilt

import dev.whyoleg.ffi.c.*

internal class PrebuiltLibcrypto3Loader : LibraryLoader {
    override val key: String get() = "crypto"
    override val dependencies: List<String> get() = emptyList()
    override fun load() {
        val loader = EmbeddedLibraryLoader.Current
        loader.loadLibrary(
            when (loader) {
                EmbeddedLibraryLoader.JVM.WINDOWS_X64 -> "crypto-3-x64"
                else                                  -> "crypto"
            }
        )
    }
}
