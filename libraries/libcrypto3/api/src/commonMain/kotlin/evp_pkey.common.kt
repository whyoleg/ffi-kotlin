@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*

class EVP_PKEY private constructor() : COpaque() {
    companion object Type : CType.Opaque<EVP_PKEY>(EVP_PKEY())
}

expect fun EVP_PKEY_keygen_init(
    ctx: CPointer<EVP_PKEY_CTX>?,
): Int

expect fun EVP_PKEY_generate(
    ctx: CPointer<EVP_PKEY_CTX>?,
    ppkey: CPointer<CPointer<EVP_PKEY>>?,
): Int

expect fun EVP_PKEY_up_ref(
    pkey: CPointer<EVP_PKEY>?,
): Int

expect fun EVP_PKEY_free(
    pkey: CPointer<EVP_PKEY>?,
)
