@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*
import kotlin.wasm.*

actual fun EVP_PKEY_CTX_new_from_name(
    libctx: CPointer<OSSL_LIB_CTX>?,
    name: CString?,
    propquery: CString?,
): CPointer<EVP_PKEY_CTX>? = nativeCPointer(
    EVP_PKEY_CTX_Type, ffi_EVP_PKEY_CTX_new_from_name(libctx.nativeAddress, name.nativeAddress, propquery.nativeAddress)
)

actual fun EVP_PKEY_CTX_set_params(
    ctx: CPointer<EVP_PKEY_CTX>?,
    params: CPointer<OSSL_PARAM>?,
): Int {
    return ffi_EVP_PKEY_CTX_set_params(ctx.nativeAddress, params.nativeAddress)
}

actual fun EVP_PKEY_CTX_free(ctx: CPointer<EVP_PKEY_CTX>?) {
    ffi_EVP_PKEY_CTX_free(ctx.nativeAddress)
}

@WasmImport("ffi-libcrypto", "ffi_EVP_PKEY_CTX_new_from_name")
private external fun ffi_EVP_PKEY_CTX_new_from_name(libctx: Int, name: Int, propquery: Int): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_PKEY_CTX_set_params")
private external fun ffi_EVP_PKEY_CTX_set_params(ctx: Int, params: Int): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_PKEY_CTX_free")
private external fun ffi_EVP_PKEY_CTX_free(ctx: Int)
