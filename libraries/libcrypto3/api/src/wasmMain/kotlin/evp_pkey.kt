@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*
import kotlin.wasm.*

actual object EVP_PKEY_Type : COpaqueType<EVP_PKEY>(::EVP_PKEY)
actual class EVP_PKEY(memory: NativeMemory) : COpaque(memory) {
    override val type: EVP_PKEY_Type get() = EVP_PKEY_Type
}

actual fun EVP_PKEY_keygen_init(ctx: CPointer<EVP_PKEY_CTX>?): Int {
    return ffi_EVP_PKEY_keygen_init(ctx.nativePointer)
}

actual fun EVP_PKEY_generate(
    ctx: CPointer<EVP_PKEY_CTX>?,
    ppkey: CPointer<CPointerVariable<EVP_PKEY>>?,
): Int {
    return ffi_EVP_PKEY_generate(ctx.nativePointer, ppkey.nativePointer)
}

actual fun EVP_PKEY_up_ref(pkey: CPointer<EVP_PKEY>?): Int {
    return ffi_EVP_PKEY_up_ref(pkey.nativePointer)
}

actual fun EVP_PKEY_free(pkey: CPointer<EVP_PKEY>?) {
    ffi_EVP_PKEY_free(pkey.nativePointer)
}

@WasmImport("ffi-libcrypto", "ffi_EVP_PKEY_keygen_init")
private external fun ffi_EVP_PKEY_keygen_init(ctx: Int): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_PKEY_generate")
private external fun ffi_EVP_PKEY_generate(ctx: Int, ppkey: Int): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_PKEY_up_ref")
private external fun ffi_EVP_PKEY_up_ref(pkey: Int): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_PKEY_free")
private external fun ffi_EVP_PKEY_free(pkey: Int)
