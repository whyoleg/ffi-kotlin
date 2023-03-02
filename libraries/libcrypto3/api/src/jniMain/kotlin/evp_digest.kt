@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

actual fun EVP_DigestSignInit_ex(
    ctx: CPointer<EVP_MD_CTX>?,
    pctx: CPointer<CPointerVariable<EVP_PKEY_CTX>>?,
    mdname: CString?,
    libctx: CPointer<OSSL_LIB_CTX>?,
    props: CString?,
    pkey: CPointer<EVP_PKEY>?,
    params: CPointer<OSSL_PARAM>?,
): Int {
    return evpdigest.EVP_DigestSignInit_ex(
        ctx.nativeAddress,
        pctx.nativeAddress,
        mdname.nativeAddress,
        libctx.nativeAddress,
        props.nativeAddress,
        pkey.nativeAddress,
        params.nativeAddress,
    )
}

actual fun EVP_DigestSignUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    data: CPointer<*>?,
    dsize: PlatformDependentUInt,
): Int {
    return evpdigest.EVP_DigestSignUpdate(ctx.nativeAddress, data.nativeAddress, dsize.toLong())
}

actual fun EVP_DigestSignFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    sigret: CPointer<UByteVariable>?,
    siglen: CPointer<PlatformDependentUIntVariable>?,
): Int {
    return evpdigest.EVP_DigestSignFinal(ctx.nativeAddress, sigret.nativeAddress, siglen.nativeAddress)
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
    return evpdigest.EVP_DigestVerifyInit_ex(
        ctx.nativeAddress,
        pctx.nativeAddress,
        mdname.nativeAddress,
        libctx.nativeAddress,
        props.nativeAddress,
        pkey.nativeAddress,
        params.nativeAddress,
    )
}

actual fun EVP_DigestVerifyUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    data: CPointer<*>?,
    dsize: PlatformDependentUInt,
): Int {
    return evpdigest.EVP_DigestVerifyUpdate(ctx.nativeAddress, data.nativeAddress, dsize.toLong())
}

actual fun EVP_DigestVerifyFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    sig: CPointer<UByteVariable>?,
    siglen: PlatformDependentUInt,
): Int {
    return evpdigest.EVP_DigestVerifyFinal(ctx.nativeAddress, sig.nativeAddress, siglen.toLong())
}

private object evpdigest {
    init {
        JNI
    }

    @JvmStatic
    external fun EVP_DigestSignInit_ex(
        ctx: Long,
        pctx: Long,
        mdname: Long,
        libctx: Long,
        props: Long,
        pkey: Long,
        params: Long,
    ): Int

    @JvmStatic
    external fun EVP_DigestSignUpdate(ctx: Long, data: Long, dsize: Long): Int

    @JvmStatic
    external fun EVP_DigestSignFinal(ctx: Long, sigret: Long, siglen: Long): Int

    @JvmStatic
    external fun EVP_DigestVerifyInit_ex(
        ctx: Long,
        pctx: Long,
        mdname: Long,
        libctx: Long,
        props: Long,
        pkey: Long,
        params: Long,
    ): Int

    @JvmStatic
    external fun EVP_DigestVerifyUpdate(ctx: Long, data: Long, dsize: Long): Int

    @JvmStatic
    external fun EVP_DigestVerifyFinal(ctx: Long, sig: Long, siglen: Long): Int

}
