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
    return evpdigest.EVP_DigestSignUpdate(ctx.address, data.address, dsize.toInt())
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
    return evpdigest.EVP_DigestVerifyUpdate(ctx.address, data.address, dsize.toInt())
}

actual fun EVP_DigestVerifyFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    sig: CPointer<UByte>?,
    siglen: PlatformUInt,
): Int = libCrypto3ImplicitScope.unsafe {
    return evpdigest.EVP_DigestVerifyFinal(ctx.address, sig.address, siglen.toInt())
}

@JsModule("foreign-crypto-wasm")
@JsNonModule
@JsName("Module")
private external object evpdigest {

    @JsName("_ffi_EVP_DigestSignInit_ex")
    fun EVP_DigestSignInit_ex(
        ctx: Int,
        pctx: Int,
        mdname: Int,
        libctx: Int,
        props: Int,
        pkey: Int,
        params: Int,
    ): Int

    @JsName("_ffi_EVP_DigestSignUpdate")
    fun EVP_DigestSignUpdate(ctx: Int, data: Int, dsize: Int): Int

    @JsName("_ffi_EVP_DigestSignFinal")
    fun EVP_DigestSignFinal(ctx: Int, sigret: Int, siglen: Int): Int

    @JsName("_ffi_EVP_DigestVerifyInit_ex")
    fun EVP_DigestVerifyInit_ex(
        ctx: Int,
        pctx: Int,
        mdname: Int,
        libctx: Int,
        props: Int,
        pkey: Int,
        params: Int,
    ): Int

    @JsName("_ffi_EVP_DigestVerifyUpdate")
    fun EVP_DigestVerifyUpdate(ctx: Int, data: Int, dsize: Int): Int

    @JsName("_ffi_EVP_DigestVerifyFinal")
    fun EVP_DigestVerifyFinal(ctx: Int, sig: Int, siglen: Int): Int

}
