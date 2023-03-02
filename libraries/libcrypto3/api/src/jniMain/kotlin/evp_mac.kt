@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

actual fun EVP_MAC_fetch(
    libctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: CString?,
    properties: CString?,
): CPointer<EVP_MAC>? = nativeCPointer(
    EVP_MAC_Type, evpmac.EVP_MAC_fetch(libctx.nativeAddress, algorithm.nativeAddress, properties.nativeAddress)
)

actual fun EVP_MAC_CTX_new(mac: CPointer<EVP_MAC>?): CPointer<EVP_MAC_CTX>? {
    return nativeCPointer(EVP_MAC_CTX_Type, evpmac.EVP_MAC_CTX_new(mac.nativeAddress))
}

actual fun EVP_MAC_init(
    ctx: CPointer<EVP_MAC_CTX>?,
    key: CPointer<UByteVariable>?,
    keylen: PlatformDependentUInt,
    params: CPointer<OSSL_PARAM>?,
): Int {
    return evpmac.EVP_MAC_init(ctx.nativeAddress, key.nativeAddress, keylen.toLong(), params.nativeAddress)
}

actual fun EVP_MAC_CTX_get_mac_size(
    ctx: CPointer<EVP_MAC_CTX>?,
): PlatformDependentUInt {
    return evpmac.EVP_MAC_CTX_get_mac_size(ctx.nativeAddress).toULong()
}

actual fun EVP_MAC_update(
    ctx: CPointer<EVP_MAC_CTX>?,
    data: CPointer<UByteVariable>?,
    datalen: PlatformDependentUInt,
): Int {
    return evpmac.EVP_MAC_update(ctx.nativeAddress, data.nativeAddress, datalen.toLong())
}

actual fun EVP_MAC_final(
    ctx: CPointer<EVP_MAC_CTX>?,
    out: CPointer<UByteVariable>?,
    outl: CPointer<PlatformDependentUIntVariable>?,
    outsize: PlatformDependentUInt,
): Int {
    return evpmac.EVP_MAC_final(ctx.nativeAddress, out.nativeAddress, outl.nativeAddress, outsize.toLong())
}

actual fun EVP_MAC_CTX_free(ctx: CPointer<EVP_MAC_CTX>?) {
    evpmac.EVP_MAC_CTX_free(ctx.nativeAddress)
}

actual fun EVP_MAC_free(ctx: CPointer<EVP_MAC>?) {
    evpmac.EVP_MAC_free(ctx.nativeAddress)
}

private object evpmac {
    init {
        JNI
    }

    @JvmStatic
    external fun EVP_MAC_fetch(libctx: Long, algorithm: Long, properties: Long): Long

    @JvmStatic
    external fun EVP_MAC_free(ctx: Long)

    @JvmStatic
    external fun EVP_MAC_init(ctx: Long, key: Long, keylen: Long, params: Long): Int

    @JvmStatic
    external fun EVP_MAC_update(ctx: Long, data: Long, datalen: Long): Int

    @JvmStatic
    external fun EVP_MAC_final(ctx: Long, out: Long, outl: Long, outsize: Long): Int

    @JvmStatic
    external fun EVP_MAC_CTX_new(mac: Long): Long

    @JvmStatic
    external fun EVP_MAC_CTX_free(ctx: Long)

    @JvmStatic
    external fun EVP_MAC_CTX_get_mac_size(ctx: Long): Long
}
