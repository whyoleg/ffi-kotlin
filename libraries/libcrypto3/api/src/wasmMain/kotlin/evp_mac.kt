@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*
import kotlin.wasm.*

actual fun EVP_MAC_fetch(
    libctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: CString?,
    properties: CString?,
): CPointer<EVP_MAC>? = nativeCPointer(
    EVP_MAC_Type, ffi_EVP_MAC_fetch(libctx.nativeAddress, algorithm.nativeAddress, properties.nativeAddress)
)

actual fun EVP_MAC_CTX_new(mac: CPointer<EVP_MAC>?): CPointer<EVP_MAC_CTX>? {
    return nativeCPointer(EVP_MAC_CTX_Type, ffi_EVP_MAC_CTX_new(mac.nativeAddress))
}

actual fun EVP_MAC_init(
    ctx: CPointer<EVP_MAC_CTX>?,
    key: CPointer<UByteVariable>?,
    keylen: PlatformDependentUInt,
    params: CPointer<OSSL_PARAM>?,
): Int {
    return ffi_EVP_MAC_init(ctx.nativeAddress, key.nativeAddress, keylen.toInt(), params.nativeAddress)
}

actual fun EVP_MAC_CTX_get_mac_size(
    ctx: CPointer<EVP_MAC_CTX>?,
): PlatformDependentUInt {
    return ffi_EVP_MAC_CTX_get_mac_size(ctx.nativeAddress).toUInt()
}

actual fun EVP_MAC_update(
    ctx: CPointer<EVP_MAC_CTX>?,
    data: CPointer<UByteVariable>?,
    datalen: PlatformDependentUInt,
): Int {
    return ffi_EVP_MAC_update(ctx.nativeAddress, data.nativeAddress, datalen.toInt())
}

actual fun EVP_MAC_final(
    ctx: CPointer<EVP_MAC_CTX>?,
    out: CPointer<UByteVariable>?,
    outl: CPointer<PlatformDependentUIntVariable>?,
    outsize: PlatformDependentUInt,
): Int {
    return ffi_EVP_MAC_final(ctx.nativeAddress, out.nativeAddress, outl.nativeAddress, outsize.toInt())
}

actual fun EVP_MAC_CTX_free(ctx: CPointer<EVP_MAC_CTX>?) {
    ffi_EVP_MAC_CTX_free(ctx.nativeAddress)
}

actual fun EVP_MAC_free(ctx: CPointer<EVP_MAC>?) {
    ffi_EVP_MAC_free(ctx.nativeAddress)
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

