@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.invoke.*
import dev.whyoleg.foreign.platform.*
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
): CPointer<EVP_MD>? = libCrypto3ImplicitScope.unsafe {
    val address = EVP_MD_fetch_MH.invokeExact(
        ctx.address,
        algorithm.address,
        properties.address,
    ) as MemorySegment
    CPointer(EVP_MD, address)
}

private val EVP_MD_CTX_new_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MD_CTX_new",
    result = ValueLayout.ADDRESS,
)

actual fun EVP_MD_CTX_new(): CPointer<EVP_MD_CTX>? = libCrypto3ImplicitScope.unsafe {
    CPointer(EVP_MD_CTX, EVP_MD_CTX_new_MH.invokeExact() as MemorySegment)
}

private val EVP_MD_get_size_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MD_get_size",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_MD_get_size(md: CPointer<EVP_MD>?): Int = libCrypto3ImplicitScope.unsafe {
    EVP_MD_get_size_MH.invokeExact(md.address) as Int
}

private val EVP_DigestInit_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_DigestInit",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS)
)

actual fun EVP_DigestInit(
    ctx: CPointer<EVP_MD_CTX>?,
    type: CPointer<EVP_MD>?,
): Int = libCrypto3ImplicitScope.unsafe {
    EVP_DigestInit_MH.invokeExact(ctx.address, type.address) as Int
}

private val EVP_DigestUpdate_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_DigestUpdate",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG)
)

actual fun EVP_DigestUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    d: CPointer<*>?,
    cnt: PlatformUInt,
): Int = libCrypto3ImplicitScope.unsafe {
    EVP_DigestUpdate_MH.invokeExact(ctx.address, d.address, cnt.toLong()) as Int
}

private val EVP_DigestFinal_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_DigestFinal",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS)
)

actual fun EVP_DigestFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    md: CPointer<UByte>?,
    s: CPointer<UInt>?,
): Int = libCrypto3ImplicitScope.unsafe {
    EVP_DigestFinal_MH.invokeExact(ctx.address, md.address, s.address) as Int
}

private val EVP_MD_CTX_free_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MD_CTX_free",
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_MD_CTX_free(ctx: CPointer<EVP_MD_CTX>?): Unit = libCrypto3ImplicitScope.unsafe {
    EVP_MD_CTX_free_MH.invokeExact(ctx.address)
}

private val EVP_MD_free_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MD_free",
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_MD_free(ctx: CPointer<EVP_MD>?): Unit = libCrypto3ImplicitScope.unsafe {
    EVP_MD_free_MH.invokeExact(ctx.address)
}
