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
//    return evpdigest.EVP_DigestSignInit_ex(
//        ctx.nativePointer,
//        pctx.nativePointer,
//        mdname.nativePointer,
//        libctx.nativePointer,
//        props.nativePointer,
//        pkey.nativePointer,
//        params.nativePointer,
//    )
}

actual fun EVP_DigestSignUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    data: CPointer<*>?,
    dsize: CULong,
): Int {
    TODO()
//    return evpdigest.EVP_DigestSignUpdate(ctx.nativePointer, data.nativePointer, dsize.toLong())
}

actual fun EVP_DigestSignFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    sigret: CPointer<CUByteVariable>?,
    siglen: CPointer<CULongVariable>?,
): Int {
    TODO()
//    return evpdigest.EVP_DigestSignFinal(ctx.nativePointer, sigret.nativePointer, siglen.nativePointer)
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
//    return evpdigest.EVP_DigestVerifyInit_ex(
//        ctx.nativePointer,
//        pctx.nativePointer,
//        mdname.nativePointer,
//        libctx.nativePointer,
//        props.nativePointer,
//        pkey.nativePointer,
//        params.nativePointer,
//    )
}

actual fun EVP_DigestVerifyUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    data: CPointer<*>?,
    dsize: CULong,
): Int {
    TODO()
//    return evpdigest.EVP_DigestVerifyUpdate(ctx.nativePointer, data.nativePointer, dsize.toLong())
}

actual fun EVP_DigestVerifyFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    sig: CPointer<CUByteVariable>?,
    siglen: CULong,
): Int {
    TODO()
//    return evpdigest.EVP_DigestVerifyFinal(ctx.nativePointer, sig.nativePointer, siglen.toLong())
}

//private object evpdigest {
//    init {
//        JNI
//    }
//
//    @JvmStatic
//    external fun EVP_DigestSignInit_ex(
//        ctx: Long,
//        pctx: Long,
//        mdname: Long,
//        libctx: Long,
//        props: Long,
//        pkey: Long,
//        params: Long,
//    ): Int
//
//    @JvmStatic
//    external fun EVP_DigestSignUpdate(ctx: Long, data: Long, dsize: Long): Int
//
//    @JvmStatic
//    external fun EVP_DigestSignFinal(ctx: Long, sigret: Long, siglen: Long): Int
//
//    @JvmStatic
//    external fun EVP_DigestVerifyInit_ex(
//        ctx: Long,
//        pctx: Long,
//        mdname: Long,
//        libctx: Long,
//        props: Long,
//        pkey: Long,
//        params: Long,
//    ): Int
//
//    @JvmStatic
//    external fun EVP_DigestVerifyUpdate(ctx: Long, data: Long, dsize: Long): Int
//
//    @JvmStatic
//    external fun EVP_DigestVerifyFinal(ctx: Long, sig: Long, siglen: Long): Int
//
//}
