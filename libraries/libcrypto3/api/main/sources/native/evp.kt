@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
    "ACTUAL_TYPE_ALIAS_NOT_TO_CLASS",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

actual typealias OSSL_LIB_CTX = dev.whyoleg.ffi.libcrypto3.cinterop.OSSL_LIB_CTX

actual typealias EVP_MD = dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MD
actual typealias EVP_MD_CTX = dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MD_CTX

actual typealias EVP_MAC = dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC
actual typealias EVP_MAC_CTX = dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC_CTX

actual fun CInteropScope.EVP_MD_fetch(
    ctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: String?,
    properties: String?,
): CPointer<EVP_MD>? {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MD_fetch(ctx, algorithm, properties)
}

actual fun CInteropScope.EVP_MD_CTX_new(): CPointer<EVP_MD_CTX>? {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MD_CTX_new()
}

actual fun CInteropScope.EVP_MD_get_size(md: CPointer<EVP_MD>?): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MD_get_size(md)
}

actual fun CInteropScope.EVP_DigestInit(
    ctx: CPointer<EVP_MD_CTX>?,
    type: CPointer<EVP_MD>?,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_DigestInit(ctx, type)
}

actual fun CInteropScope.EVP_DigestUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    d: CPointer<*>?,
    cnt: CULong,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_DigestUpdate(ctx, d, cnt)
}

actual fun CInteropScope.EVP_DigestFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    md: CPointer<CUByteVariable>?,
    s: CPointer<CUIntVariable>?,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_DigestFinal(ctx, md, s)
}

actual fun CInteropScope.EVP_MD_CTX_free(ctx: CPointer<EVP_MD_CTX>?) {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MD_CTX_free(ctx)
}

actual fun CInteropScope.EVP_MD_free(ctx: CPointer<EVP_MD>?) {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MD_free(ctx)
}

actual fun CInteropScope.EVP_MAC_fetch(
    libctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: String?,
    properties: String?,
): CPointer<EVP_MAC>? {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC_fetch(libctx, algorithm, properties)
}

actual fun CInteropScope.EVP_MAC_CTX_new(mac: CPointer<EVP_MAC>?): CPointer<EVP_MAC_CTX>? {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC_CTX_new(mac)
}

actual fun CInteropScope.EVP_MAC_init(
    ctx: CPointer<EVP_MAC_CTX>?,
    key: CPointer<CUByteVariable>?,
    keylen: CULong,
    params: CPointer<OSSL_PARAM>?,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC_init(ctx, key, keylen, params)
}

actual fun CInteropScope.EVP_MAC_CTX_get_mac_size(
    ctx: CPointer<EVP_MAC_CTX>?,
): CULong {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC_CTX_get_mac_size(ctx)
}

actual fun CInteropScope.EVP_MAC_update(
    ctx: CPointer<EVP_MAC_CTX>?,
    data: CPointer<CUByteVariable>?,
    datalen: CULong,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC_update(ctx, data, datalen)
}

actual fun CInteropScope.EVP_MAC_final(
    ctx: CPointer<EVP_MAC_CTX>?,
    out: CPointer<CUByteVariable>?,
    outl: CPointer<CULongVariable>?,
    outsize: CULong,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC_final(ctx, out, outl, outsize)
}

actual fun EVP_MAC_CTX_free(ctx: CPointer<EVP_MAC_CTX>?) {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC_CTX_free(ctx)
}

actual fun EVP_MAC_free(ctx: CPointer<EVP_MAC>?) {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MAC_free(ctx)
}

actual fun CInteropScope.OSSL_PARAM_construct_utf8_string(
    key: String?,
    buf: CString?,
    bsize: CULong,
): CValue<OSSL_PARAM> {
    return dev.whyoleg.ffi.libcrypto3.cinterop.OSSL_PARAM_construct_utf8_string(key?.let(::alloc), buf, bsize)
}

actual fun CInteropScope.OSSL_PARAM_construct_end(): CValue<OSSL_PARAM> {
    return dev.whyoleg.ffi.libcrypto3.cinterop.OSSL_PARAM_construct_end()
}
