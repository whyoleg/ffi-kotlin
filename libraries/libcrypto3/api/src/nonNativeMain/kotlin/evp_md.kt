@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

actual object EVP_MD_Type : COpaqueType<EVP_MD>() {
    override fun wrap(memory: NativeMemory): EVP_MD = EVP_MD(memory)
}

actual class EVP_MD(memory: NativeMemory) : COpaque(memory) {
    override val type: EVP_MD_Type get() = EVP_MD_Type
}

actual object EVP_MD_CTX_Type : COpaqueType<EVP_MD_CTX>() {
    override fun wrap(memory: NativeMemory): EVP_MD_CTX = EVP_MD_CTX(memory)
}

actual class EVP_MD_CTX(memory: NativeMemory) : COpaque(memory) {
    override val type: EVP_MD_CTX_Type get() = EVP_MD_CTX_Type
}
