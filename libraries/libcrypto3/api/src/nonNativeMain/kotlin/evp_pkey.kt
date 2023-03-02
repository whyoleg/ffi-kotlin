@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

actual object EVP_PKEY_Type : COpaqueType<EVP_PKEY>() {
    override fun wrap(memory: NativeMemory): EVP_PKEY = EVP_PKEY(memory)
}

actual class EVP_PKEY(memory: NativeMemory) : COpaque(memory) {
    override val type: EVP_PKEY_Type get() = EVP_PKEY_Type
}
