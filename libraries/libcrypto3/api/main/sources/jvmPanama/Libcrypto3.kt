package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

internal object Libcrypto3 : FFI() {
    init {
        System.loadLibrary("crypto")
    }
}
