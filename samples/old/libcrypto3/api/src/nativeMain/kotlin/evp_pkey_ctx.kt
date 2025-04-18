@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import kotlinx.cinterop.internal.*

actual fun EVP_PKEY_CTX_new_from_name(
    libctx: CPointer<OSSL_LIB_CTX>?,
    name: CString?,
    propquery: CString?,
): CPointer<EVP_PKEY_CTX>? = libCrypto3ImplicitScope.unsafe {
    CPointer(EVP_PKEY_CTX, ffi_EVP_PKEY_CTX_new_from_name(libctx.address, name.address, propquery.address))
}

actual fun EVP_PKEY_CTX_set_params(
    ctx: CPointer<EVP_PKEY_CTX>?,
    params: CArrayPointer<OSSL_PARAM>?,
): Int = libCrypto3ImplicitScope.unsafe {
    ffi_EVP_PKEY_CTX_set_params(ctx.address, params.address)
}

actual fun EVP_PKEY_CTX_free(ctx: CPointer<EVP_PKEY_CTX>?) = libCrypto3ImplicitScope.unsafe {
    ffi_EVP_PKEY_CTX_free(ctx.address)
}


@CCall("ffi_EVP_PKEY_CTX_new_from_name")
private external fun ffi_EVP_PKEY_CTX_new_from_name(libctx: Long, name: Long, propquery: Long): Long

@CCall("ffi_EVP_PKEY_CTX_set_params")
private external fun ffi_EVP_PKEY_CTX_set_params(ctx: Long, params: Long): Int

@CCall("ffi_EVP_PKEY_CTX_free")
private external fun ffi_EVP_PKEY_CTX_free(ctx: Long)
