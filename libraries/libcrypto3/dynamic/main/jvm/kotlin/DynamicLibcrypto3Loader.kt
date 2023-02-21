package dev.whyoleg.ffi.libcrypto3.dynamic

import dev.whyoleg.ffi.*

internal class DynamicLibcrypto3Loader : LibraryLoader() {
    override fun load() {
        System.loadLibrary("crypto")
    }
}
