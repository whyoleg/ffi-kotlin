@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

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
        evpmac.EVP_MAC_fetch(libctx.nativePointer, algorithm.nativePointer, properties.nativePointer)
    ), EVP_MAC_Type
)

actual fun EVP_MAC_CTX_new(mac: CPointer<EVP_MAC>?): CPointer<EVP_MAC_CTX>? {
    return CPointer(NativePointer(evpmac.EVP_MAC_CTX_new(mac.nativePointer)), EVP_MAC_CTX_Type)
}

actual fun EVP_MAC_init(
    ctx: CPointer<EVP_MAC_CTX>?,
    key: CPointer<UByteVariable>?,
    keylen: PlatformDependentUInt,
    params: CPointer<OSSL_PARAM>?,
): Int {
    return evpmac.EVP_MAC_init(ctx.nativePointer, key.nativePointer, keylen.toInt(), params.nativePointer)
}

actual fun EVP_MAC_CTX_get_mac_size(
    ctx: CPointer<EVP_MAC_CTX>?,
): PlatformDependentUInt {
    return evpmac.EVP_MAC_CTX_get_mac_size(ctx.nativePointer).toUInt()
}

actual fun EVP_MAC_update(
    ctx: CPointer<EVP_MAC_CTX>?,
    data: CPointer<UByteVariable>?,
    datalen: PlatformDependentUInt,
): Int {
    return evpmac.EVP_MAC_update(ctx.nativePointer, data.nativePointer, datalen.toInt())
}

actual fun EVP_MAC_final(
    ctx: CPointer<EVP_MAC_CTX>?,
    out: CPointer<UByteVariable>?,
    outl: CPointer<PlatformDependentUIntVariable>?,
    outsize: PlatformDependentUInt,
): Int {
    return evpmac.EVP_MAC_final(ctx.nativePointer, out.nativePointer, outl.nativePointer, outsize.toInt())
}

actual fun EVP_MAC_CTX_free(ctx: CPointer<EVP_MAC_CTX>?) {
    evpmac.EVP_MAC_CTX_free(ctx.nativePointer)
}

actual fun EVP_MAC_free(ctx: CPointer<EVP_MAC>?) {
    evpmac.EVP_MAC_free(ctx.nativePointer)
}

@JsModule("ffi-libcrypto")
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
