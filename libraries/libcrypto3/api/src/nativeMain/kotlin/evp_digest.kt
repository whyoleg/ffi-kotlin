@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual fun EVP_DigestSignInit_ex(
    ctx: CPointer<EVP_MD_CTX>?,
    pctx: CPointer<CPointerVariable<EVP_PKEY_CTX>>?,
    mdname: CString?,
    libctx: CPointer<OSSL_LIB_CTX>?,
    props: CString?,
    pkey: CPointer<EVP_PKEY>?,
    params: CPointer<OSSL_PARAM>?,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_DigestSignInit_ex(
        ctx,
        pctx,
        mdname?.toKString(),
        libctx,
        props?.toKString(),
        pkey,
        params
    )
}

actual fun EVP_DigestSignUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    data: CPointer<*>?,
    dsize: PlatformDependentUInt,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_DigestSignUpdate(ctx, data, dsize)
}

actual fun EVP_DigestSignFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    sigret: CPointer<UByteVariable>?,
    siglen: CPointer<PlatformDependentUIntVariable>?,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_DigestSignFinal(ctx, sigret, siglen)
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual fun EVP_DigestVerifyInit_ex(
    ctx: CPointer<EVP_MD_CTX>?,
    pctx: CPointer<CPointerVariable<EVP_PKEY_CTX>>?,
    mdname: CString?,
    libctx: CPointer<OSSL_LIB_CTX>?,
    props: CString?,
    pkey: CPointer<EVP_PKEY>?,
    params: CPointer<OSSL_PARAM>?,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_DigestVerifyInit_ex(
        ctx,
        pctx,
        mdname?.toKString(),
        libctx,
        props?.toKString(),
        pkey,
        params
    )
}

actual fun EVP_DigestVerifyUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    data: CPointer<*>?,
    dsize: PlatformDependentUInt,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_DigestVerifyUpdate(ctx, data, dsize)
}

actual fun EVP_DigestVerifyFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    sig: CPointer<UByteVariable>?,
    siglen: PlatformDependentUInt,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_DigestVerifyFinal(ctx, sig, siglen)
}
