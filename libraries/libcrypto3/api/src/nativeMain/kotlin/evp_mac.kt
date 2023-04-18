@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.platform.*

actual fun EVP_MAC_fetch(
    libctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: CString?,
    properties: CString?,
): CPointer<EVP_MAC>? {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC_fetch(libctx, algorithm?.toKString(), properties?.toKString())
}

actual fun EVP_MAC_CTX_new(mac: CPointer<EVP_MAC>?): CPointer<EVP_MAC_CTX>? {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC_CTX_new(mac)
}

actual fun EVP_MAC_init(
    ctx: CPointer<EVP_MAC_CTX>?,
    key: CPointer<UByte>?,
    keylen: PlatformUInt,
    params: CArrayPointer<OSSL_PARAM>?,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC_init(ctx, key, keylen, params)
}

actual fun EVP_MAC_CTX_get_mac_size(
    ctx: CPointer<EVP_MAC_CTX>?,
): PlatformUInt {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC_CTX_get_mac_size(ctx)
}

actual fun EVP_MAC_update(
    ctx: CPointer<EVP_MAC_CTX>?,
    data: CPointer<UByte>?,
    datalen: PlatformUInt,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC_update(ctx, data, datalen)
}

actual fun EVP_MAC_final(
    ctx: CPointer<EVP_MAC_CTX>?,
    out: CPointer<UByte>?,
    outl: CPointer<PlatformUInt>?,
    outsize: PlatformUInt,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC_final(ctx, out, outl, outsize)
}

actual fun EVP_MAC_CTX_free(ctx: CPointer<EVP_MAC_CTX>?) {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC_CTX_free(ctx)
}

actual fun EVP_MAC_free(ctx: CPointer<EVP_MAC>?) {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC_free(ctx)
}
