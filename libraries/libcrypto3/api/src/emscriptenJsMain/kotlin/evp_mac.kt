@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.platform.*

actual fun EVP_MAC_fetch(
    libctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: CString?,
    properties: CString?,
): CPointer<EVP_MAC>? = libCrypto3ImplicitScope.unsafe {
    val address = evpmac.EVP_MAC_fetch(libctx.address, algorithm.address, properties.address)
    CPointer(EVP_MAC, address)
}

actual fun EVP_MAC_CTX_new(mac: CPointer<EVP_MAC>?): CPointer<EVP_MAC_CTX>? = libCrypto3ImplicitScope.unsafe {
    val address = evpmac.EVP_MAC_CTX_new(mac.address)
    CPointer(EVP_MAC_CTX, address)
}

actual fun EVP_MAC_init(
    ctx: CPointer<EVP_MAC_CTX>?,
    key: CPointer<UByte>?,
    keylen: PlatformUInt,
    params: CArrayPointer<OSSL_PARAM>?,
): Int = libCrypto3ImplicitScope.unsafe {
    evpmac.EVP_MAC_init(ctx.address, key.address, keylen.toInt(), params.address)
}

actual fun EVP_MAC_CTX_get_mac_size(
    ctx: CPointer<EVP_MAC_CTX>?,
): PlatformUInt = libCrypto3ImplicitScope.unsafe {
    evpmac.EVP_MAC_CTX_get_mac_size(ctx.address).toUInt()
}

actual fun EVP_MAC_update(
    ctx: CPointer<EVP_MAC_CTX>?,
    data: CPointer<UByte>?,
    datalen: PlatformUInt,
): Int = libCrypto3ImplicitScope.unsafe {
    evpmac.EVP_MAC_update(ctx.address, data.address, datalen.toInt())
}

actual fun EVP_MAC_final(
    ctx: CPointer<EVP_MAC_CTX>?,
    out: CPointer<UByte>?,
    outl: CPointer<PlatformUInt>?,
    outsize: PlatformUInt,
): Int = libCrypto3ImplicitScope.unsafe {
    evpmac.EVP_MAC_final(ctx.address, out.address, outl.address, outsize.toInt())
}

actual fun EVP_MAC_CTX_free(ctx: CPointer<EVP_MAC_CTX>?): Unit = libCrypto3ImplicitScope.unsafe {
    evpmac.EVP_MAC_CTX_free(ctx.address)
}

actual fun EVP_MAC_free(ctx: CPointer<EVP_MAC>?): Unit = libCrypto3ImplicitScope.unsafe {
    evpmac.EVP_MAC_free(ctx.address)
}

@JsModule("foreign-crypto-wasm")
@JsNonModule
@JsName("Module")
private external object evpmac {
    @JsName("_ffi_EVP_MAC_fetch")
    fun EVP_MAC_fetch(libctx: Int, algorithm: Int, properties: Int): Int

    @JsName("_ffi_EVP_MAC_free")
    fun EVP_MAC_free(ctx: Int)

    @JsName("_ffi_EVP_MAC_init")
    fun EVP_MAC_init(ctx: Int, key: Int, keylen: Int, params: Int): Int

    @JsName("_ffi_EVP_MAC_update")
    fun EVP_MAC_update(ctx: Int, data: Int, datalen: Int): Int

    @JsName("_ffi_EVP_MAC_final")
    fun EVP_MAC_final(ctx: Int, out: Int, outl: Int, outsize: Int): Int

    @JsName("_ffi_EVP_MAC_CTX_new")
    fun EVP_MAC_CTX_new(mac: Int): Int

    @JsName("_ffi_EVP_MAC_CTX_free")
    fun EVP_MAC_CTX_free(ctx: Int)

    @JsName("_ffi_EVP_MAC_CTX_get_mac_size")
    fun EVP_MAC_CTX_get_mac_size(ctx: Int): Int

}
