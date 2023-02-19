@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

expect class OSSL_LIB_CTX : COpaque


expect class EVP_MD : COpaque
expect class EVP_MD_CTX : COpaque

expect class EVP_MAC : COpaque
expect class EVP_MAC_CTX : COpaque

//TODO: scope?

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

expect fun CInteropScope.EVP_MAC_fetch(
    libctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: String?,
    properties: String?,
): CPointer<EVP_MAC>?

expect fun CInteropScope.EVP_MAC_CTX_new(
    mac: CPointer<EVP_MAC>?,
): CPointer<EVP_MAC_CTX>?

expect fun CInteropScope.EVP_MAC_init(
    ctx: CPointer<EVP_MAC_CTX>?,
    key: CPointer<CUByteVariable>?,
    keylen: CULong,
    params: CPointer<OSSL_PARAM>?,
): Int

expect fun CInteropScope.EVP_MAC_CTX_get_mac_size(
    ctx: CPointer<EVP_MAC_CTX>?,
): CULong

expect fun CInteropScope.EVP_MAC_update(
    ctx: CPointer<EVP_MAC_CTX>?,
    data: CPointer<CUByteVariable>?,
    datalen: CULong,
): Int

expect fun CInteropScope.EVP_MAC_final(
    ctx: CPointer<EVP_MAC_CTX>?,
    out: CPointer<CUByteVariable>?,
    outl: CPointer<CULongVariable>?,
    outsize: CULong,
): Int

expect fun EVP_MAC_CTX_free(
    ctx: CPointer<EVP_MAC_CTX>?,
)

expect fun EVP_MAC_free(
    ctx: CPointer<EVP_MAC>?,
)

expect fun CInteropScope.OSSL_PARAM_construct_utf8_string(
    key: String?,
    buf: CString?,
    bsize: CULong,
): CValue<OSSL_PARAM>

expect fun CInteropScope.OSSL_PARAM_construct_end(): CValue<OSSL_PARAM>
