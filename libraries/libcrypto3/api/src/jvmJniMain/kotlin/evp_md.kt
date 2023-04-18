@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.platform.*

actual fun EVP_MD_fetch(
    ctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: CString?,
    properties: CString?,
): CPointer<EVP_MD>? = libCrypto3ImplicitScope.unsafe {
    val address = evpmd.EVP_MD_fetch(ctx.address, algorithm.address, properties.address)
    CPointer(EVP_MD, address)
}

actual fun EVP_MD_CTX_new(): CPointer<EVP_MD_CTX>? = libCrypto3ImplicitScope.unsafe {
    CPointer(EVP_MD_CTX, evpmd.EVP_MD_CTX_new())
}

actual fun EVP_MD_get_size(md: CPointer<EVP_MD>?): Int = libCrypto3ImplicitScope.unsafe {
    evpmd.EVP_MD_get_size(md.address)
}

actual fun EVP_DigestInit(
    ctx: CPointer<EVP_MD_CTX>?,
    type: CPointer<EVP_MD>?,
): Int = libCrypto3ImplicitScope.unsafe {
    evpmd.EVP_DigestInit(ctx.address, type.address)
}

actual fun EVP_DigestUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    d: CPointer<*>?,
    cnt: PlatformUInt,
): Int = libCrypto3ImplicitScope.unsafe {
    evpmd.EVP_DigestUpdate(ctx.address, d.address, cnt.toLong())
}

actual fun EVP_DigestFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    md: CPointer<UByte>?,
    s: CPointer<UInt>?,
): Int = libCrypto3ImplicitScope.unsafe {
    evpmd.EVP_DigestFinal(ctx.address, md.address, s.address)
}

actual fun EVP_MD_CTX_free(ctx: CPointer<EVP_MD_CTX>?) = libCrypto3ImplicitScope.unsafe {
    evpmd.EVP_MD_CTX_free(ctx.address)
}

actual fun EVP_MD_free(ctx: CPointer<EVP_MD>?) = libCrypto3ImplicitScope.unsafe {
    evpmd.EVP_MD_free(ctx.address)
}

private object evpmd {
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
