package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.memory.*

actual val ForeignMemory.Companion.LibCrypto3: ForeignMemory by lazy {
//    EmbeddedLibraryLoader.Current.loadLibrary("foreign-crypto-jni")
    ForeignMemory.Default
}
