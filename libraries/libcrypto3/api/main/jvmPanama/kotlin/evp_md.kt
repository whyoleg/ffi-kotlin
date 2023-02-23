@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*
import java.lang.foreign.*
import java.lang.invoke.*

actual class EVP_MD(segment: MemorySegment) : COpaque(segment)
actual object EVP_MD_Type : COpaqueType<EVP_MD>(::EVP_MD)
actual class EVP_MD_CTX(segment: MemorySegment) : COpaque(segment)
actual object EVP_MD_CTX_Type : COpaqueType<EVP_MD_CTX>(::EVP_MD_CTX)

private val EVP_MD_fetch_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MD_fetch",
    result = ValueLayout.ADDRESS,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS)
)

actual fun EVP_MD_fetch(
    ctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: CString?,
    properties: CString?,
): CPointer<EVP_MD>? = CPointer(
    EVP_MD_fetch_MH.invokeExact(
        ctx.segment,
        algorithm.segment,
        properties.segment,
    ) as MemorySegment,
    EVP_MD_Type
)

private val EVP_MD_CTX_new_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MD_CTX_new",
    result = ValueLayout.ADDRESS,
)

actual fun EVP_MD_CTX_new(): CPointer<EVP_MD_CTX>? {
    return CPointer(EVP_MD_CTX_new_MH.invokeExact() as MemorySegment, EVP_MD_CTX_Type)
}

private val EVP_MD_get_size_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MD_get_size",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_MD_get_size(md: CPointer<EVP_MD>?): Int {
    return EVP_MD_get_size_MH.invokeExact(md.segment) as Int
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
    return EVP_DigestInit_MH.invokeExact(ctx.segment, type.segment) as Int
}

private val EVP_DigestUpdate_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_DigestUpdate",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG)
)

actual fun EVP_DigestUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    d: CPointer<*>?,
    cnt: CULong,
): Int {
    return EVP_DigestUpdate_MH.invokeExact(ctx.segment, d.segment, cnt.toLong()) as Int
}

private val EVP_DigestFinal_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_DigestFinal",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS)
)

actual fun EVP_DigestFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    md: CPointer<CUByteVariable>?,
    s: CPointer<CUIntVariable>?,
): Int {
    return EVP_DigestFinal_MH.invokeExact(ctx.segment, md.segment, s.segment) as Int
}

private val EVP_MD_CTX_free_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MD_CTX_free",
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_MD_CTX_free(ctx: CPointer<EVP_MD_CTX>?) {
    EVP_MD_CTX_free_MH.invokeExact(ctx.segment)
}

private val EVP_MD_free_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MD_free",
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_MD_free(ctx: CPointer<EVP_MD>?) {
    EVP_MD_free_MH.invokeExact(ctx.segment)
}
