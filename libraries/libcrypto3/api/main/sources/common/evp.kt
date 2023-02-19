package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

expect class OSSL_LIB_CTX : COpaque

//expect class OSSL_PARAM : CStructVariable
expect class EVP_MD : COpaque
expect class EVP_MD_CTX : COpaque
//expect class EVP_MAC_CTX : COpaque

expect fun CInteropScope.EVP_MD_fetch(
    ctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: String?,
    properties: String?,
): CPointer<EVP_MD>?

expect fun CInteropScope.EVP_MD_CTX_new(): CPointer<EVP_MD_CTX>?

expect fun CInteropScope.EVP_MD_get_size(md: CPointer<EVP_MD>?): Int

expect fun CInteropScope.EVP_DigestInit(
    ctx: CPointer<EVP_MD_CTX>?,
    type: CPointer<EVP_MD>?,
): Int

expect fun CInteropScope.EVP_DigestUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    d: CPointer<*>?,
    cnt: CULong,
): Int

expect fun CInteropScope.EVP_DigestFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    md: CPointer<CUByteVariable>?,
    s: CPointer<CUIntVariable>?,
): Int

expect fun CInteropScope.EVP_MD_CTX_free(ctx: CPointer<EVP_MD_CTX>?)

expect fun CInteropScope.EVP_MD_free(ctx: CPointer<EVP_MD>?)
//
//
//
//expect fun EVP_MAC_init(
//  ctx: CPointer<EVP_MAC_CTX>?,
//  key: CPointer<UByteVar>?,
//  keylen: size_t,
//  params: CPointer<OSSL_PARAM>?
//): Int
//
//expect fun OSSL_PARAM_construct_utf8_string(
//  key: String?,
//  buf: CPointer<ByteVar>?,
//  bsize: size_t
//): CValue<OSSL_PARAM>
