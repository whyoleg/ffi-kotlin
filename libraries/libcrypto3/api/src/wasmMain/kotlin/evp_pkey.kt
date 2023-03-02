@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*
import kotlin.wasm.*

actual fun EVP_PKEY_keygen_init(ctx: CPointer<EVP_PKEY_CTX>?): Int {
    return ffi_EVP_PKEY_keygen_init(ctx.nativeAddress)
}

actual fun EVP_PKEY_generate(
    ctx: CPointer<EVP_PKEY_CTX>?,
    ppkey: CPointer<CPointerVariable<EVP_PKEY>>?,
): Int {
    return ffi_EVP_PKEY_generate(ctx.nativeAddress, ppkey.nativeAddress)
}

actual fun EVP_PKEY_up_ref(pkey: CPointer<EVP_PKEY>?): Int {
    return ffi_EVP_PKEY_up_ref(pkey.nativeAddress)
}

actual fun EVP_PKEY_free(pkey: CPointer<EVP_PKEY>?) {
    ffi_EVP_PKEY_free(pkey.nativeAddress)
}

@WasmImport("ffi-libcrypto", "ffi_EVP_PKEY_keygen_init")
private external fun ffi_EVP_PKEY_keygen_init(ctx: Int): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_PKEY_generate")
private external fun ffi_EVP_PKEY_generate(ctx: Int, ppkey: Int): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_PKEY_up_ref")
private external fun ffi_EVP_PKEY_up_ref(pkey: Int): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_PKEY_free")
private external fun ffi_EVP_PKEY_free(pkey: Int)
