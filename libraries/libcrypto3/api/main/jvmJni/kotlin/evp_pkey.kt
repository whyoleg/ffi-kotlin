@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

actual object EVP_PKEY_Type : COpaqueType<EVP_PKEY>(::EVP_PKEY)
actual class EVP_PKEY(memory: NativeMemory) : COpaque(memory) {
    override val type: EVP_PKEY_Type get() = EVP_PKEY_Type
}

actual fun EVP_PKEY_keygen_init(ctx: CPointer<EVP_PKEY_CTX>?): Int {
    TODO()
}

actual fun EVP_PKEY_generate(
    ctx: CPointer<EVP_PKEY_CTX>?,
    ppkey: CPointer<CPointerVariable<EVP_PKEY>>?,
): Int {
    TODO()
}

actual fun EVP_PKEY_up_ref(pkey: CPointer<EVP_PKEY>?): Int {
    TODO()
}

actual fun EVP_PKEY_free(pkey: CPointer<EVP_PKEY>?) {
    TODO()
}
