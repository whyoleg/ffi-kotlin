@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.platform.*

class EVP_MD private constructor() : COpaque() {
    companion object Type : CType.Opaque<EVP_MD>(EVP_MD())
}

class EVP_MD_CTX private constructor() : COpaque() {
    companion object Type : CType.Opaque<EVP_MD_CTX>(EVP_MD_CTX())
}

expect fun EVP_MD_fetch(
    ctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: CString?,
    properties: CString?,
): CPointer<EVP_MD>?

expect fun EVP_MD_CTX_new(): CPointer<EVP_MD_CTX>?

expect fun EVP_MD_get_size(md: CPointer<EVP_MD>?): Int

expect fun EVP_DigestInit(
    ctx: CPointer<EVP_MD_CTX>?,
    type: CPointer<EVP_MD>?,
): Int

expect fun EVP_DigestUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    d: CPointer<*>?,
    cnt: PlatformUInt,
): Int

expect fun EVP_DigestFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    md: CPointer<UByte>?,
    s: CPointer<UInt>?,
): Int

expect fun EVP_MD_CTX_free(ctx: CPointer<EVP_MD_CTX>?)

expect fun EVP_MD_free(ctx: CPointer<EVP_MD>?)
