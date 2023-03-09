@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

expect class EVP_MAC : COpaque {
    companion object Type : COpaque.Type<EVP_MAC>
}

expect class EVP_MAC_CTX : COpaque {
    companion object Type : COpaque.Type<EVP_MAC_CTX>
}

expect fun EVP_MAC_fetch(
    libctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: CString?,
    properties: CString?,
): CPointer<EVP_MAC>?

expect fun EVP_MAC_CTX_new(
    mac: CPointer<EVP_MAC>?,
): CPointer<EVP_MAC_CTX>?

expect fun EVP_MAC_init(
    ctx: CPointer<EVP_MAC_CTX>?,
    key: CPointer<CUByte>?,
    keylen: PlatformUInt,
    params: CPointer<OSSL_PARAM>?,
): Int

expect fun EVP_MAC_CTX_get_mac_size(
    ctx: CPointer<EVP_MAC_CTX>?,
): PlatformUInt

expect fun EVP_MAC_update(
    ctx: CPointer<EVP_MAC_CTX>?,
    data: CPointer<CUByte>?,
    datalen: PlatformUInt,
): Int

expect fun EVP_MAC_final(
    ctx: CPointer<EVP_MAC_CTX>?,
    out: CPointer<CUByte>?,
    outl: CPointer<CPlatformUInt>?,
    outsize: PlatformUInt,
): Int

expect fun EVP_MAC_CTX_free(
    ctx: CPointer<EVP_MAC_CTX>?,
)

expect fun EVP_MAC_free(
    ctx: CPointer<EVP_MAC>?,
)
