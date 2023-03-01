@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*
import kotlin.wasm.*

actual fun EVP_DigestSignInit_ex(
    ctx: CPointer<EVP_MD_CTX>?,
    pctx: CPointer<CPointerVariable<EVP_PKEY_CTX>>?,
    mdname: CString?,
    libctx: CPointer<OSSL_LIB_CTX>?,
    props: CString?,
    pkey: CPointer<EVP_PKEY>?,
    params: CPointer<OSSL_PARAM>?,
): Int {
    return ffi_EVP_DigestSignInit_ex(
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
    dsize: PlatformDependentUInt,
): Int {
    return ffi_EVP_DigestSignUpdate(ctx.nativePointer, data.nativePointer, dsize.toInt())
}

actual fun EVP_DigestSignFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    sigret: CPointer<UByteVariable>?,
    siglen: CPointer<PlatformDependentUIntVariable>?,
): Int {
    return ffi_EVP_DigestSignFinal(ctx.nativePointer, sigret.nativePointer, siglen.nativePointer)
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
    return ffi_EVP_DigestVerifyInit_ex(
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
    dsize: PlatformDependentUInt,
): Int {
    return ffi_EVP_DigestVerifyUpdate(ctx.nativePointer, data.nativePointer, dsize.toInt())
}

actual fun EVP_DigestVerifyFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    sig: CPointer<UByteVariable>?,
    siglen: PlatformDependentUInt,
): Int {
    return ffi_EVP_DigestVerifyFinal(ctx.nativePointer, sig.nativePointer, siglen.toInt())
}


@WasmImport("ffi-libcrypto", "ffi_EVP_DigestSignInit_ex")
private external fun ffi_EVP_DigestSignInit_ex(
    ctx: Int,
    pctx: Int,
    mdname: Int,
    libctx: Int,
    props: Int,
    pkey: Int,
    params: Int,
): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_DigestSignUpdate")
private external fun ffi_EVP_DigestSignUpdate(ctx: Int, data: Int, dsize: Int): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_DigestSignFinal")
private external fun ffi_EVP_DigestSignFinal(ctx: Int, sigret: Int, siglen: Int): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_DigestVerifyInit_ex")
private external fun ffi_EVP_DigestVerifyInit_ex(
    ctx: Int,
    pctx: Int,
    mdname: Int,
    libctx: Int,
    props: Int,
    pkey: Int,
    params: Int,
): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_DigestVerifyUpdate")
private external fun ffi_EVP_DigestVerifyUpdate(ctx: Int, data: Int, dsize: Int): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_DigestVerifyFinal")
private external fun ffi_EVP_DigestVerifyFinal(ctx: Int, sig: Int, siglen: Int): Int

