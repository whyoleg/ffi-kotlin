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
    return evpdigest.EVP_DigestSignInit_ex(
        ctx.nativePointer,
        pctx.nativePointer,
        mdname.nativePointer,
        libctx.nativePointer,
        props.nativePointer,
        pkey.nativePointer,
        params.nativePointer,
    )
}

actual fun EVP_DigestSignUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    data: CPointer<*>?,
    dsize: ULong,
): Int {
    return evpdigest.EVP_DigestSignUpdate(ctx.nativePointer, data.nativePointer, dsize.toInt())
}

actual fun EVP_DigestSignFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    sigret: CPointer<UByteVariable>?,
    siglen: CPointer<ULongVariable>?,
): Int {
    return evpdigest.EVP_DigestSignFinal(ctx.nativePointer, sigret.nativePointer, siglen.nativePointer)
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
        ctx.nativePointer,
        pctx.nativePointer,
        mdname.nativePointer,
        libctx.nativePointer,
        props.nativePointer,
        pkey.nativePointer,
        params.nativePointer,
    )
}

actual fun EVP_DigestVerifyUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    data: CPointer<*>?,
    dsize: ULong,
): Int {
    return evpdigest.EVP_DigestVerifyUpdate(ctx.nativePointer, data.nativePointer, dsize.toInt())
}

actual fun EVP_DigestVerifyFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    sig: CPointer<UByteVariable>?,
    siglen: ULong,
): Int {
    return evpdigest.EVP_DigestVerifyFinal(ctx.nativePointer, sig.nativePointer, siglen.toInt())
}

@JsModule("ffi-libcrypto")
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
