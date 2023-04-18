@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.platform.*

actual fun EVP_PKEY_CTX_new_from_name(
    libctx: CPointer<OSSL_LIB_CTX>?,
    name: CString?,
    propquery: CString?,
): CPointer<EVP_PKEY_CTX>? {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_PKEY_CTX_new_from_name(libctx, name?.toKString(), propquery?.toKString())
}

actual fun EVP_PKEY_CTX_set_params(
    ctx: CPointer<EVP_PKEY_CTX>?,
    params: CArrayPointer<OSSL_PARAM>?,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_PKEY_CTX_set_params(ctx, params)
}

actual fun EVP_PKEY_CTX_free(ctx: CPointer<EVP_PKEY_CTX>?) {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_PKEY_CTX_free(ctx)
}
