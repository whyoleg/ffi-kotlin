@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
    "ACTUAL_TYPE_ALIAS_NOT_TO_CLASS",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

actual typealias EVP_PKEY_CTX = dev.whyoleg.ffi.libcrypto3.cinterop.EVP_PKEY_CTX

actual object EVP_PKEY_CTX_Type : COpaqueType<EVP_PKEY_CTX>()

actual fun EVP_PKEY_CTX_new_from_name(
    libctx: CPointer<OSSL_LIB_CTX>?,
    name: CString?,
    propquery: CString?,
): CPointer<EVP_PKEY_CTX>? {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_PKEY_CTX_new_from_name(libctx, name?.toKString(), propquery?.toKString())
}

actual fun EVP_PKEY_CTX_set_params(
    ctx: CPointer<EVP_PKEY_CTX>?,
    params: CPointer<OSSL_PARAM>?,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_PKEY_CTX_set_params(ctx, params)
}

actual fun EVP_PKEY_CTX_free(ctx: CPointer<EVP_PKEY_CTX>?) {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_PKEY_CTX_free(ctx)
}
