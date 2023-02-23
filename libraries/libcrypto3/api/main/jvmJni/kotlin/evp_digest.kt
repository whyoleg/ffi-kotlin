@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

actual fun EVP_DigestSignInit_ex(
    ctx: CPointer<EVP_MD_CTX>?,
    pctx: CPointer<CPointerVariable<EVP_PKEY_CTX>>?,
    mdname: CString?,
    libctx: CPointer<OSSL_LIB_CTX>?,
    props: CString?,
    pkey: CPointer<EVP_PKEY>?,
    params: CPointer<OSSL_PARAM>?,
): Int {
    TODO()
}

actual fun EVP_DigestSignUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    data: CPointer<*>?,
    dsize: CULong,
): Int {
    TODO()
}

actual fun EVP_DigestSignFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    sigret: CPointer<CUByteVariable>?,
    siglen: CPointer<CULongVariable>?,
): Int {
    TODO()
}

actual fun EVP_DigestVerifyInit_ex(
    ctx: CPointer<EVP_MD_CTX>?,
    pctx: CPointer<CPointerVariable<EVP_PKEY_CTX>>?,
    mdname: CString?,
    libctx: CPointer<OSSL_LIB_CTX>?,
    props: CString?,
    pkey: CPointer<EVP_PKEY>?,
    params: CPointer<OSSL_PARAM>?,
): Int {
    TODO()
}

actual fun EVP_DigestVerifyUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    data: CPointer<*>?,
    dsize: CULong,
): Int {
    TODO()
}

actual fun EVP_DigestVerifyFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    sig: CPointer<CUByteVariable>?,
    siglen: CULong,
): Int {
    TODO()
}
