@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

expect class EVP_PKEY : COpaque

expect fun EVP_PKEY_keygen_init(
    ctx: CPointer<EVP_PKEY_CTX>?,
): Int

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect fun EVP_PKEY_generate(
    ctx: CPointer<EVP_PKEY_CTX>?,
    ppkey: CPointer<CPointerVariable<EVP_PKEY>>?,
): Int

expect fun EVP_PKEY_up_ref(
    pkey: CPointer<EVP_PKEY>?,
): Int

expect fun EVP_PKEY_free(
    pkey: CPointer<EVP_PKEY>?,
): Unit
