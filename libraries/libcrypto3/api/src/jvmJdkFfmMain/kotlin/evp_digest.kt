@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.invoke.*
import dev.whyoleg.foreign.platform.*
import java.lang.foreign.*
import java.lang.invoke.*

private val EVP_DigestSignInit_ex: MethodHandle = FFI.methodHandle(
    name = "EVP_DigestSignInit_ex",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(
        ValueLayout.ADDRESS,
        ValueLayout.ADDRESS,
        ValueLayout.ADDRESS,
        ValueLayout.ADDRESS,
        ValueLayout.ADDRESS,
        ValueLayout.ADDRESS,
        ValueLayout.ADDRESS,
    )
)

actual fun EVP_DigestSignInit_ex(
    ctx: CPointer<EVP_MD_CTX>?,
    pctx: CPointer<CPointer<EVP_PKEY_CTX>>?,
    mdname: CString?,
    libctx: CPointer<OSSL_LIB_CTX>?,
    props: CString?,
    pkey: CPointer<EVP_PKEY>?,
    params: CArrayPointer<OSSL_PARAM>?,
): Int = libCrypto3ImplicitScope.unsafe {
    EVP_DigestSignInit_ex.invokeExact(
        ctx.address,
        pctx.address,
        mdname.address,
        libctx.address,
        props.address,
        pkey.address,
        params.address,
    ) as Int
}

private val EVP_DigestSignUpdate: MethodHandle = FFI.methodHandle(
    name = "EVP_DigestSignUpdate",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG)
)

actual fun EVP_DigestSignUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    data: CPointer<*>?,
    dsize: PlatformUInt,
): Int = libCrypto3ImplicitScope.unsafe {
    EVP_DigestSignUpdate.invokeExact(
        ctx.address,
        data.address,
        dsize.toLong()
    ) as Int
}

private val EVP_DigestSignFinal: MethodHandle = FFI.methodHandle(
    name = "EVP_DigestSignFinal",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS)
)

actual fun EVP_DigestSignFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    sigret: CPointer<UByte>?,
    siglen: CPointer<PlatformUInt>?,
): Int = libCrypto3ImplicitScope.unsafe {
    EVP_DigestSignFinal.invokeExact(
        ctx.address,
        sigret.address,
        siglen.address
    ) as Int
}

private val EVP_DigestVerifyInit_ex: MethodHandle = FFI.methodHandle(
    name = "EVP_DigestVerifyInit_ex",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(
        ValueLayout.ADDRESS,
        ValueLayout.ADDRESS,
        ValueLayout.ADDRESS,
        ValueLayout.ADDRESS,
        ValueLayout.ADDRESS,
        ValueLayout.ADDRESS,
        ValueLayout.ADDRESS,
    )
)

actual fun EVP_DigestVerifyInit_ex(
    ctx: CPointer<EVP_MD_CTX>?,
    pctx: CPointer<CPointer<EVP_PKEY_CTX>>?,
    mdname: CString?,
    libctx: CPointer<OSSL_LIB_CTX>?,
    props: CString?,
    pkey: CPointer<EVP_PKEY>?,
    params: CArrayPointer<OSSL_PARAM>?,
): Int = libCrypto3ImplicitScope.unsafe {
    EVP_DigestVerifyInit_ex.invokeExact(
        ctx.address,
        pctx.address,
        mdname.address,
        libctx.address,
        props.address,
        pkey.address,
        params.address,
    ) as Int
}

private val EVP_DigestVerifyUpdate: MethodHandle = FFI.methodHandle(
    name = "EVP_DigestVerifyUpdate",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG)
)

actual fun EVP_DigestVerifyUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    data: CPointer<*>?,
    dsize: PlatformUInt,
): Int = libCrypto3ImplicitScope.unsafe {
    EVP_DigestVerifyUpdate.invokeExact(
        ctx.address,
        data.address,
        dsize.toLong()
    ) as Int
}

private val EVP_DigestVerifyFinal: MethodHandle = FFI.methodHandle(
    name = "EVP_DigestVerifyFinal",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG)
)

actual fun EVP_DigestVerifyFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    sig: CPointer<UByte>?,
    siglen: PlatformUInt,
): Int = libCrypto3ImplicitScope.unsafe {
    EVP_DigestVerifyFinal.invokeExact(
        ctx.address,
        sig.address,
        siglen.toLong()
    ) as Int
}
