@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*
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
    pctx: CPointer<CPointerVariable<EVP_PKEY_CTX>>?,
    mdname: CString?,
    libctx: CPointer<OSSL_LIB_CTX>?,
    props: CString?,
    pkey: CPointer<EVP_PKEY>?,
    params: CPointer<OSSL_PARAM>?,
): Int {
    return EVP_DigestSignInit_ex.invokeExact(
        ctx.nativeAddress,
        pctx.nativeAddress,
        mdname.nativeAddress,
        libctx.nativeAddress,
        props.nativeAddress,
        pkey.nativeAddress,
        params.nativeAddress,
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
    dsize: PlatformDependentUInt,
): Int {
    return EVP_DigestSignUpdate.invokeExact(
        ctx.nativeAddress,
        data.nativeAddress,
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
    sigret: CPointer<UByteVariable>?,
    siglen: CPointer<PlatformDependentUIntVariable>?,
): Int {
    return EVP_DigestSignFinal.invokeExact(
        ctx.nativeAddress,
        sigret.nativeAddress,
        siglen.nativeAddress
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
    pctx: CPointer<CPointerVariable<EVP_PKEY_CTX>>?,
    mdname: CString?,
    libctx: CPointer<OSSL_LIB_CTX>?,
    props: CString?,
    pkey: CPointer<EVP_PKEY>?,
    params: CPointer<OSSL_PARAM>?,
): Int {
    return EVP_DigestVerifyInit_ex.invokeExact(
        ctx.nativeAddress,
        pctx.nativeAddress,
        mdname.nativeAddress,
        libctx.nativeAddress,
        props.nativeAddress,
        pkey.nativeAddress,
        params.nativeAddress,
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
    dsize: PlatformDependentUInt,
): Int {
    return EVP_DigestVerifyUpdate.invokeExact(
        ctx.nativeAddress,
        data.nativeAddress,
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
    sig: CPointer<UByteVariable>?,
    siglen: PlatformDependentUInt,
): Int {
    return EVP_DigestVerifyFinal.invokeExact(
        ctx.nativeAddress,
        sig.nativeAddress,
        siglen.toLong()
    ) as Int
}
