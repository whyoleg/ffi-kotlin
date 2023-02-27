@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

expect class EVP_MAC : COpaque
expect object EVP_MAC_Type : COpaqueType<EVP_MAC>
expect class EVP_MAC_CTX : COpaque
expect object EVP_MAC_CTX_Type : COpaqueType<EVP_MAC_CTX>

//TODO: scope?

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
    key: CPointer<UByteVariable>?,
    keylen: ULong,
    params: CPointer<OSSL_PARAM>?,
): Int

expect fun EVP_MAC_CTX_get_mac_size(
    ctx: CPointer<EVP_MAC_CTX>?,
): ULong

expect fun EVP_MAC_update(
    ctx: CPointer<EVP_MAC_CTX>?,
    data: CPointer<UByteVariable>?,
    datalen: ULong,
): Int

expect fun EVP_MAC_final(
    ctx: CPointer<EVP_MAC_CTX>?,
    out: CPointer<UByteVariable>?,
    outl: CPointer<ULongVariable>?,
    outsize: ULong,
): Int

expect fun EVP_MAC_CTX_free(
    ctx: CPointer<EVP_MAC_CTX>?,
)

expect fun EVP_MAC_free(
    ctx: CPointer<EVP_MAC>?,
)
