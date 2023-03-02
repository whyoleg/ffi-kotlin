@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

actual fun EVP_MD_fetch(
    ctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: CString?,
    properties: CString?,
): CPointer<EVP_MD>? = nativeCPointer(
    EVP_MD_Type, evpmd.EVP_MD_fetch(ctx.nativeAddress, algorithm.nativeAddress, properties.nativeAddress)
)

actual fun EVP_MD_CTX_new(): CPointer<EVP_MD_CTX>? {
    return nativeCPointer(EVP_MD_CTX_Type, evpmd.EVP_MD_CTX_new())
}

actual fun EVP_MD_get_size(md: CPointer<EVP_MD>?): Int {
    return evpmd.EVP_MD_get_size(md.nativeAddress)
}

actual fun EVP_DigestInit(
    ctx: CPointer<EVP_MD_CTX>?,
    type: CPointer<EVP_MD>?,
): Int {
    return evpmd.EVP_DigestInit(ctx.nativeAddress, type.nativeAddress)
}

actual fun EVP_DigestUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    d: CPointer<*>?,
    cnt: PlatformDependentUInt,
): Int {
    return evpmd.EVP_DigestUpdate(ctx.nativeAddress, d.nativeAddress, cnt.toLong())
}

actual fun EVP_DigestFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    md: CPointer<UByteVariable>?,
    s: CPointer<UIntVariable>?,
): Int {
    return evpmd.EVP_DigestFinal(ctx.nativeAddress, md.nativeAddress, s.nativeAddress)
}

actual fun EVP_MD_CTX_free(ctx: CPointer<EVP_MD_CTX>?) {
    evpmd.EVP_MD_CTX_free(ctx.nativeAddress)
}

actual fun EVP_MD_free(ctx: CPointer<EVP_MD>?) {
    evpmd.EVP_MD_free(ctx.nativeAddress)
}

private object evpmd {
    init {
        JNI
    }

    @JvmStatic
    external fun EVP_MD_fetch(ctx: Long, algorithm: Long, properties: Long): Long

    @JvmStatic
    external fun EVP_MD_free(md: Long)

    @JvmStatic
    external fun EVP_MD_get_size(md: Long): Int

    @JvmStatic
    external fun EVP_MD_CTX_new(): Long

    @JvmStatic
    external fun EVP_MD_CTX_free(ctx: Long)

    @JvmStatic
    external fun EVP_DigestInit(ctx: Long, type: Long): Int

    @JvmStatic
    external fun EVP_DigestUpdate(ctx: Long, d: Long, cnt: Long): Int

    @JvmStatic
    external fun EVP_DigestFinal(ctx: Long, md: Long, s: Long): Int
}
