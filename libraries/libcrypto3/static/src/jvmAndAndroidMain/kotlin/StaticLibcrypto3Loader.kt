package dev.whyoleg.ffi.libcrypto3.static

import dev.whyoleg.ffi.*

internal class StaticLibcrypto3Loader : LibraryLoader {
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
