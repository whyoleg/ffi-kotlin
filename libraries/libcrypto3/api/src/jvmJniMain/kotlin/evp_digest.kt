@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.platform.*

actual fun EVP_DigestSignInit_ex(
    ctx: CPointer<EVP_MD_CTX>?,
    pctx: CPointer<CPointer<EVP_PKEY_CTX>>?,
    mdname: CString?,
    libctx: CPointer<OSSL_LIB_CTX>?,
    props: CString?,
    pkey: CPointer<EVP_PKEY>?,
    params: CArrayPointer<OSSL_PARAM>?,
): Int = libCrypto3ImplicitScope.unsafe {
    return evpdigest.EVP_DigestSignInit_ex(
        ctx.address,
        pctx.address,
        mdname.address,
        libctx.address,
        props.address,
        pkey.address,
        params.address,
    )
}

actual fun EVP_DigestSignUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    data: CPointer<*>?,
    dsize: PlatformUInt,
): Int = libCrypto3ImplicitScope.unsafe {
    return evpdigest.EVP_DigestSignUpdate(ctx.address, data.address, dsize.toLong())
}

actual fun EVP_DigestSignFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    sigret: CPointer<UByte>?,
    siglen: CPointer<PlatformUInt>?,
): Int = libCrypto3ImplicitScope.unsafe {
    return evpdigest.EVP_DigestSignFinal(ctx.address, sigret.address, siglen.address)
}

actual fun EVP_DigestVerifyInit_ex(
    ctx: CPointer<EVP_MD_CTX>?,
    pctx: CPointer<CPointer<EVP_PKEY_CTX>>?,
    mdname: CString?,
    libctx: CPointer<OSSL_LIB_CTX>?,
    props: CString?,
    pkey: CPointer<EVP_PKEY>?,
    params: CArrayPointer<OSSL_PARAM>?,
): Int = libCrypto3ImplicitScope.unsafe {
    return evpdigest.EVP_DigestVerifyInit_ex(
        ctx.address,
        pctx.address,
        mdname.address,
        libctx.address,
        props.address,
        pkey.address,
        params.address,
    )
}

actual fun EVP_DigestVerifyUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    data: CPointer<*>?,
    dsize: PlatformUInt,
): Int = libCrypto3ImplicitScope.unsafe {
    return evpdigest.EVP_DigestVerifyUpdate(ctx.address, data.address, dsize.toLong())
}

actual fun EVP_DigestVerifyFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    sig: CPointer<UByte>?,
    siglen: PlatformUInt,
): Int = libCrypto3ImplicitScope.unsafe {
    return evpdigest.EVP_DigestVerifyFinal(ctx.address, sig.address, siglen.toLong())
}

private object evpdigest {

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
