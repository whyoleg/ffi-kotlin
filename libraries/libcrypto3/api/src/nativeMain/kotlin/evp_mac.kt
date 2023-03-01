@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
    "ACTUAL_TYPE_ALIAS_NOT_TO_CLASS",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

actual typealias EVP_MAC = dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC
actual object EVP_MAC_Type : COpaqueType<EVP_MAC>()
actual typealias EVP_MAC_CTX = dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC_CTX

actual object EVP_MAC_CTX_Type : COpaqueType<EVP_MAC_CTX>()

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
    key: CPointer<UByteVariable>?,
    keylen: PlatformDependentUInt,
    params: CPointer<OSSL_PARAM>?,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC_init(ctx, key, keylen, params)
}

actual fun EVP_MAC_CTX_get_mac_size(
    ctx: CPointer<EVP_MAC_CTX>?,
): PlatformDependentUInt {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC_CTX_get_mac_size(ctx)
}

actual fun EVP_MAC_update(
    ctx: CPointer<EVP_MAC_CTX>?,
    data: CPointer<UByteVariable>?,
    datalen: PlatformDependentUInt,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC_update(ctx, data, datalen)
}

actual fun EVP_MAC_final(
    ctx: CPointer<EVP_MAC_CTX>?,
    out: CPointer<UByteVariable>?,
    outl: CPointer<PlatformDependentUIntVariable>?,
    outsize: PlatformDependentUInt,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC_final(ctx, out, outl, outsize)
}

actual fun EVP_MAC_CTX_free(ctx: CPointer<EVP_MAC_CTX>?) {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC_CTX_free(ctx)
}

actual fun EVP_MAC_free(ctx: CPointer<EVP_MAC>?) {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC_free(ctx)
}
