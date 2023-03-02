@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

actual fun EVP_PKEY_CTX_new_from_name(
    libctx: CPointer<OSSL_LIB_CTX>?,
    name: CString?,
    propquery: CString?,
): CPointer<EVP_PKEY_CTX>? = nativeCPointer(
    EVP_PKEY_CTX_Type,
    evppkeyctx.EVP_PKEY_CTX_new_from_name(libctx.nativeAddress, name.nativeAddress, propquery.nativeAddress)
)

actual fun EVP_PKEY_CTX_set_params(
    ctx: CPointer<EVP_PKEY_CTX>?,
    params: CPointer<OSSL_PARAM>?,
): Int {
    return evppkeyctx.EVP_PKEY_CTX_set_params(ctx.nativeAddress, params.nativeAddress)
}

actual fun EVP_PKEY_CTX_free(ctx: CPointer<EVP_PKEY_CTX>?) {
    evppkeyctx.EVP_PKEY_CTX_free(ctx.nativeAddress)
}

@JsModule("ffi-libcrypto")
@JsNonModule
@JsName("Module")
private external object evppkeyctx {
    @JsName("_ffi_EVP_PKEY_CTX_new_from_name")
    fun EVP_PKEY_CTX_new_from_name(libctx: Int, name: Int, propquery: Int): Int

    @JsName("_ffi_EVP_PKEY_CTX_set_params")
    fun EVP_PKEY_CTX_set_params(ctx: Int, params: Int): Int

    @JsName("_ffi_EVP_PKEY_CTX_free")
    fun EVP_PKEY_CTX_free(ctx: Int)
}
