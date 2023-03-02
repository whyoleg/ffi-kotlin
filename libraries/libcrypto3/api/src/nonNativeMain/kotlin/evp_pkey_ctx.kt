@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

actual object EVP_PKEY_CTX_Type : COpaqueType<EVP_PKEY_CTX>() {
    override fun wrap(memory: NativeMemory): EVP_PKEY_CTX = EVP_PKEY_CTX(memory)
}

actual class EVP_PKEY_CTX(memory: NativeMemory) : COpaque(memory) {
    override val type: EVP_PKEY_CTX_Type get() = EVP_PKEY_CTX_Type
}
