@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*
import kotlin.wasm.*

actual object EVP_PKEY_CTX_Type : COpaqueType<EVP_PKEY_CTX>(::EVP_PKEY_CTX)
actual class EVP_PKEY_CTX(memory: NativeMemory) : COpaque(memory) {
    override val type: EVP_PKEY_CTX_Type get() = EVP_PKEY_CTX_Type
}

actual fun EVP_PKEY_CTX_new_from_name(
    libctx: CPointer<OSSL_LIB_CTX>?,
    name: CString?,
    propquery: CString?,
): CPointer<EVP_PKEY_CTX>? = CPointer(
    NativePointer(
        ffi_EVP_PKEY_CTX_new_from_name(libctx.nativePointer, name.nativePointer, propquery.nativePointer)
    ),
    EVP_PKEY_CTX_Type
)

actual fun EVP_PKEY_CTX_set_params(
    ctx: CPointer<EVP_PKEY_CTX>?,
    params: CPointer<OSSL_PARAM>?,
): Int {
    return ffi_EVP_PKEY_CTX_set_params(ctx.nativePointer, params.nativePointer)
}

actual fun EVP_PKEY_CTX_free(ctx: CPointer<EVP_PKEY_CTX>?) {
    ffi_EVP_PKEY_CTX_free(ctx.nativePointer)
}

@WasmImport("ffi-libcrypto", "ffi_EVP_PKEY_CTX_new_from_name")
private external fun ffi_EVP_PKEY_CTX_new_from_name(libctx: Int, name: Int, propquery: Int): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_PKEY_CTX_set_params")
private external fun ffi_EVP_PKEY_CTX_set_params(ctx: Int, params: Int): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_PKEY_CTX_free")
private external fun ffi_EVP_PKEY_CTX_free(ctx: Int)
