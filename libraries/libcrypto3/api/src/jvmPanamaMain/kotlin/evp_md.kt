@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*
import java.lang.foreign.*
import java.lang.invoke.*

private val EVP_MD_fetch_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MD_fetch",
    result = ValueLayout.ADDRESS,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS)
)

actual fun EVP_MD_fetch(
    ctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: CString?,
    properties: CString?,
): CPointer<EVP_MD>? = nativeCPointer(
    EVP_MD_Type,
    EVP_MD_fetch_MH.invokeExact(
        ctx.nativeAddress,
        algorithm.nativeAddress,
        properties.nativeAddress,
    ) as MemorySegment
)

private val EVP_MD_CTX_new_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MD_CTX_new",
    result = ValueLayout.ADDRESS,
)

actual fun EVP_MD_CTX_new(): CPointer<EVP_MD_CTX>? {
    return nativeCPointer(EVP_MD_CTX_Type, EVP_MD_CTX_new_MH.invokeExact() as MemorySegment)
}

private val EVP_MD_get_size_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MD_get_size",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_MD_get_size(md: CPointer<EVP_MD>?): Int {
    return EVP_MD_get_size_MH.invokeExact(md.nativeAddress) as Int
}

private val EVP_DigestInit_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_DigestInit",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS)
)

actual fun EVP_DigestInit(
    ctx: CPointer<EVP_MD_CTX>?,
    type: CPointer<EVP_MD>?,
): Int {
    return EVP_DigestInit_MH.invokeExact(ctx.nativeAddress, type.nativeAddress) as Int
}

private val EVP_DigestUpdate_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_DigestUpdate",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG)
)

actual fun EVP_DigestUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    d: CPointer<*>?,
    cnt: PlatformDependentUInt,
): Int {
    return EVP_DigestUpdate_MH.invokeExact(ctx.nativeAddress, d.nativeAddress, cnt.toLong()) as Int
}

private val EVP_DigestFinal_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_DigestFinal",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS)
)

actual fun EVP_DigestFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    md: CPointer<UByteVariable>?,
    s: CPointer<UIntVariable>?,
): Int {
    return EVP_DigestFinal_MH.invokeExact(ctx.nativeAddress, md.nativeAddress, s.nativeAddress) as Int
}

private val EVP_MD_CTX_free_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MD_CTX_free",
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_MD_CTX_free(ctx: CPointer<EVP_MD_CTX>?) {
    EVP_MD_CTX_free_MH.invokeExact(ctx.nativeAddress)
}

private val EVP_MD_free_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MD_free",
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_MD_free(ctx: CPointer<EVP_MD>?) {
    EVP_MD_free_MH.invokeExact(ctx.nativeAddress)
}
