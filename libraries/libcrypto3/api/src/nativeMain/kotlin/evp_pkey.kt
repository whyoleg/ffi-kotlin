@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.platform.*

actual fun EVP_PKEY_keygen_init(ctx: CPointer<EVP_PKEY_CTX>?): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_PKEY_keygen_init(ctx)
}

actual fun EVP_PKEY_generate(
    ctx: CPointer<EVP_PKEY_CTX>?,
    ppkey: CPointer<CPointer<EVP_PKEY>>?,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_PKEY_generate(ctx, ppkey)
}

actual fun EVP_PKEY_up_ref(pkey: CPointer<EVP_PKEY>?): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_PKEY_up_ref(pkey)
}

actual fun EVP_PKEY_free(pkey: CPointer<EVP_PKEY>?) {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_PKEY_free(pkey)
}
