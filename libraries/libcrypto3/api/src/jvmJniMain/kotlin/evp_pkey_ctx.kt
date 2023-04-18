@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*

actual fun EVP_PKEY_CTX_new_from_name(
    libctx: CPointer<OSSL_LIB_CTX>?,
    name: CString?,
    propquery: CString?,
): CPointer<EVP_PKEY_CTX>? = libCrypto3ImplicitScope.unsafe {
    CPointer(EVP_PKEY_CTX, evppkeyctx.EVP_PKEY_CTX_new_from_name(libctx.address, name.address, propquery.address))
}

actual fun EVP_PKEY_CTX_set_params(
    ctx: CPointer<EVP_PKEY_CTX>?,
    params: CArrayPointer<OSSL_PARAM>?,
): Int = libCrypto3ImplicitScope.unsafe {
    evppkeyctx.EVP_PKEY_CTX_set_params(ctx.address, params.address)
}

actual fun EVP_PKEY_CTX_free(ctx: CPointer<EVP_PKEY_CTX>?) = libCrypto3ImplicitScope.unsafe {
    evppkeyctx.EVP_PKEY_CTX_free(ctx.address)
}

private object evppkeyctx {
    @JvmStatic
    external fun EVP_PKEY_CTX_new_from_name(libctx: Long, name: Long, propquery: Long): Long

    @JvmStatic
    external fun EVP_PKEY_CTX_set_params(ctx: Long, params: Long): Int

    @JvmStatic
    external fun EVP_PKEY_CTX_free(ctx: Long)
}
