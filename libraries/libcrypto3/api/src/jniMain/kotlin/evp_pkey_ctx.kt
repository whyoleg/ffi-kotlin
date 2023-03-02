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
    EVP_PKEY_CTX_Type, evppkeyctx.EVP_PKEY_CTX_new_from_name(libctx.nativeAddress, name.nativeAddress, propquery.nativeAddress)
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

private object evppkeyctx {
    init {
        JNI
    }

    @JvmStatic
    external fun EVP_PKEY_CTX_new_from_name(libctx: Long, name: Long, propquery: Long): Long

    @JvmStatic
    external fun EVP_PKEY_CTX_set_params(ctx: Long, params: Long): Int

    @JvmStatic
    external fun EVP_PKEY_CTX_free(ctx: Long)
}
