@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

actual object EVP_MAC_Type : COpaqueType<EVP_MAC>() {
    override fun wrap(memory: NativeMemory): EVP_MAC = EVP_MAC(memory)
}

actual class EVP_MAC(memory: NativeMemory) : COpaque(memory) {
    override val type: EVP_MAC_Type get() = EVP_MAC_Type
}

actual object EVP_MAC_CTX_Type : COpaqueType<EVP_MAC_CTX>() {
    override fun wrap(memory: NativeMemory): EVP_MAC_CTX = EVP_MAC_CTX(memory)
}

actual class EVP_MAC_CTX(memory: NativeMemory) : COpaque(memory) {
    override val type: EVP_MAC_CTX_Type get() = EVP_MAC_CTX_Type
}
