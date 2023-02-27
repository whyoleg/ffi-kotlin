@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*
import kotlin.wasm.*

actual object EVP_MAC_Type : COpaqueType<EVP_MAC>(::EVP_MAC)
actual class EVP_MAC(memory: NativeMemory) : COpaque(memory) {
    override val type: EVP_MAC_Type get() = EVP_MAC_Type
}

actual object EVP_MAC_CTX_Type : COpaqueType<EVP_MAC_CTX>(::EVP_MAC_CTX)
actual class EVP_MAC_CTX(memory: NativeMemory) : COpaque(memory) {
    override val type: EVP_MAC_CTX_Type get() = EVP_MAC_CTX_Type
}

actual fun EVP_MAC_fetch(
    libctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: CString?,
    properties: CString?,
): CPointer<EVP_MAC>? = CPointer(
    NativePointer(
        ffi_EVP_MAC_fetch(libctx.nativePointer, algorithm.nativePointer, properties.nativePointer)
    ), EVP_MAC_Type
)

actual fun EVP_MAC_CTX_new(mac: CPointer<EVP_MAC>?): CPointer<EVP_MAC_CTX>? {
    return CPointer(NativePointer(ffi_EVP_MAC_CTX_new(mac.nativePointer)), EVP_MAC_CTX_Type)
}

actual fun EVP_MAC_init(
    ctx: CPointer<EVP_MAC_CTX>?,
    key: CPointer<UByteVariable>?,
    keylen: ULong,
    params: CPointer<OSSL_PARAM>?,
): Int {
    return ffi_EVP_MAC_init(ctx.nativePointer, key.nativePointer, keylen.toInt(), params.nativePointer)
}

actual fun EVP_MAC_CTX_get_mac_size(
    ctx: CPointer<EVP_MAC_CTX>?,
): ULong {
    return ffi_EVP_MAC_CTX_get_mac_size(ctx.nativePointer).toULong()
}

actual fun EVP_MAC_update(
    ctx: CPointer<EVP_MAC_CTX>?,
    data: CPointer<UByteVariable>?,
    datalen: ULong,
): Int {
    return ffi_EVP_MAC_update(ctx.nativePointer, data.nativePointer, datalen.toInt())
}

actual fun EVP_MAC_final(
    ctx: CPointer<EVP_MAC_CTX>?,
    out: CPointer<UByteVariable>?,
    outl: CPointer<ULongVariable>?,
    outsize: ULong,
): Int {
    return ffi_EVP_MAC_final(ctx.nativePointer, out.nativePointer, outl.nativePointer, outsize.toInt())
}

actual fun EVP_MAC_CTX_free(ctx: CPointer<EVP_MAC_CTX>?) {
    ffi_EVP_MAC_CTX_free(ctx.nativePointer)
}

actual fun EVP_MAC_free(ctx: CPointer<EVP_MAC>?) {
    ffi_EVP_MAC_free(ctx.nativePointer)
}

@WasmImport("ffi-libcrypto", "ffi_EVP_MAC_fetch")
private external fun ffi_EVP_MAC_fetch(libctx: Int, algorithm: Int, properties: Int): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_MAC_free")
private external fun ffi_EVP_MAC_free(ctx: Int)

@WasmImport("ffi-libcrypto", "ffi_EVP_MAC_init")
private external fun ffi_EVP_MAC_init(ctx: Int, key: Int, keylen: Int, params: Int): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_MAC_update")
private external fun ffi_EVP_MAC_update(ctx: Int, data: Int, datalen: Int): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_MAC_final")
private external fun ffi_EVP_MAC_final(ctx: Int, out: Int, outl: Int, outsize: Int): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_MAC_CTX_new")
private external fun ffi_EVP_MAC_CTX_new(mac: Int): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_MAC_CTX_free")
private external fun ffi_EVP_MAC_CTX_free(ctx: Int)

@WasmImport("ffi-libcrypto", "ffi_EVP_MAC_CTX_get_mac_size")
private external fun ffi_EVP_MAC_CTX_get_mac_size(ctx: Int): Int

