@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*

class EVP_PKEY_CTX private constructor() : COpaque() {
    companion object Type : CType.Opaque<EVP_PKEY_CTX>(EVP_PKEY_CTX())
}

expect fun EVP_PKEY_CTX_new_from_name(
    libctx: CPointer<OSSL_LIB_CTX>?,
    name: CString?,
    propquery: CString?,
): CPointer<EVP_PKEY_CTX>?

expect fun EVP_PKEY_CTX_set_params(
    ctx: CPointer<EVP_PKEY_CTX>?,
    params: CArrayPointer<OSSL_PARAM>?,
): Int

expect fun EVP_PKEY_CTX_free(
    ctx: CPointer<EVP_PKEY_CTX>?,
)
