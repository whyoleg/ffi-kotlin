@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.platform.*
import kotlin.wasm.*

actual fun EVP_MAC_fetch(
    libctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: CString?,
    properties: CString?,
): CPointer<EVP_MAC>? = libCrypto3ImplicitScope.unsafe {
    val address = ffi_EVP_MAC_fetch(libctx.address, algorithm.address, properties.address)
    CPointer(EVP_MAC, address)
}

actual fun EVP_MAC_CTX_new(mac: CPointer<EVP_MAC>?): CPointer<EVP_MAC_CTX>? = libCrypto3ImplicitScope.unsafe {
    val address = ffi_EVP_MAC_CTX_new(mac.address)
    CPointer(EVP_MAC_CTX, address)
}

actual fun EVP_MAC_init(
    ctx: CPointer<EVP_MAC_CTX>?,
    key: CPointer<UByte>?,
    keylen: PlatformUInt,
    params: CArrayPointer<OSSL_PARAM>?,
): Int = libCrypto3ImplicitScope.unsafe {
    ffi_EVP_MAC_init(ctx.address, key.address, keylen.toInt(), params.address)
}

actual fun EVP_MAC_CTX_get_mac_size(
    ctx: CPointer<EVP_MAC_CTX>?,
): PlatformUInt = libCrypto3ImplicitScope.unsafe {
    ffi_EVP_MAC_CTX_get_mac_size(ctx.address).toUInt()
}

actual fun EVP_MAC_update(
    ctx: CPointer<EVP_MAC_CTX>?,
    data: CPointer<UByte>?,
    datalen: PlatformUInt,
): Int = libCrypto3ImplicitScope.unsafe {
    ffi_EVP_MAC_update(ctx.address, data.address, datalen.toInt())
}

actual fun EVP_MAC_final(
    ctx: CPointer<EVP_MAC_CTX>?,
    out: CPointer<UByte>?,
    outl: CPointer<PlatformUInt>?,
    outsize: PlatformUInt,
): Int = libCrypto3ImplicitScope.unsafe {
    ffi_EVP_MAC_final(ctx.address, out.address, outl.address, outsize.toInt())
}

actual fun EVP_MAC_CTX_free(ctx: CPointer<EVP_MAC_CTX>?): Unit = libCrypto3ImplicitScope.unsafe {
    ffi_EVP_MAC_CTX_free(ctx.address)
}

actual fun EVP_MAC_free(ctx: CPointer<EVP_MAC>?): Unit = libCrypto3ImplicitScope.unsafe {
    ffi_EVP_MAC_free(ctx.address)
}


@WasmImport("foreign-crypto-wasm", "ffi_EVP_MAC_fetch")
private external fun ffi_EVP_MAC_fetch(libctx: Int, algorithm: Int, properties: Int): Int

@WasmImport("foreign-crypto-wasm", "ffi_EVP_MAC_free")
private external fun ffi_EVP_MAC_free(ctx: Int)

@WasmImport("foreign-crypto-wasm", "ffi_EVP_MAC_init")
private external fun ffi_EVP_MAC_init(ctx: Int, key: Int, keylen: Int, params: Int): Int

@WasmImport("foreign-crypto-wasm", "ffi_EVP_MAC_update")
private external fun ffi_EVP_MAC_update(ctx: Int, data: Int, datalen: Int): Int

@WasmImport("foreign-crypto-wasm", "ffi_EVP_MAC_final")
private external fun ffi_EVP_MAC_final(ctx: Int, out: Int, outl: Int, outsize: Int): Int

@WasmImport("foreign-crypto-wasm", "ffi_EVP_MAC_CTX_new")
private external fun ffi_EVP_MAC_CTX_new(mac: Int): Int

@WasmImport("foreign-crypto-wasm", "ffi_EVP_MAC_CTX_free")
private external fun ffi_EVP_MAC_CTX_free(ctx: Int)

@WasmImport("foreign-crypto-wasm", "ffi_EVP_MAC_CTX_get_mac_size")
private external fun ffi_EVP_MAC_CTX_get_mac_size(ctx: Int): Int

