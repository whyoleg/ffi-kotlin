package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

internal class Libcrypto3JniLoader : LibraryLoader() {
    override fun load() {
        loadFromResources("/libs/macos-arm64/libcrypto-jni.dylib", "dylib")
    }
}
