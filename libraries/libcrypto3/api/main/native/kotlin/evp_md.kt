@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
    "ACTUAL_TYPE_ALIAS_NOT_TO_CLASS",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

actual typealias EVP_MD = dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MD

actual object EVP_MD_Type : COpaqueType<EVP_MD>()
actual typealias EVP_MD_CTX = dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MD_CTX

actual object EVP_MD_CTX_Type : COpaqueType<EVP_MD_CTX>()

actual fun EVP_MD_fetch(
    ctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: CString?,
    properties: CString?,
): CPointer<EVP_MD>? {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MD_fetch(ctx, algorithm?.toKString(), properties?.toKString())
}

actual fun EVP_MD_CTX_new(): CPointer<EVP_MD_CTX>? {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MD_CTX_new()
}

actual fun EVP_MD_get_size(md: CPointer<EVP_MD>?): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MD_get_size(md)
}

actual fun EVP_DigestInit(
    ctx: CPointer<EVP_MD_CTX>?,
    type: CPointer<EVP_MD>?,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_DigestInit(ctx, type)
}

actual fun EVP_DigestUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    d: CPointer<*>?,
    cnt: ULong,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_DigestUpdate(ctx, d, cnt)
}

actual fun EVP_DigestFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    md: CPointer<UByteVariable>?,
    s: CPointer<UIntVariable>?,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_DigestFinal(ctx, md, s)
}

actual fun EVP_MD_CTX_free(ctx: CPointer<EVP_MD_CTX>?) {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MD_CTX_free(ctx)
}

actual fun EVP_MD_free(ctx: CPointer<EVP_MD>?) {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_MD_free(ctx)
}
