package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*
import java.lang.foreign.*
import java.lang.invoke.*

//expect class OSSL_LIB_CTX : COpaque
//expect class OSSL_PARAM : CStructVariable
//expect class EVP_MD : COpaque

actual class OSSL_LIB_CTX(segment: MemorySegment) : COpaque(segment)

actual class EVP_MD(segment: MemorySegment) : COpaque(segment)

actual class EVP_MD_CTX(segment: MemorySegment) : COpaque(segment)

private val EVP_MD_fetch_MH: MethodHandle = Runtime.methodHandle(
    name = "EVP_MD_fetch",
    result = ValueLayout.ADDRESS,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS)
)

//TODO: scopes
actual fun CInteropScope.EVP_MD_fetch(
    ctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: String?,
    properties: String?,
): CPointer<EVP_MD>? = CPointer(
    EVP_MD_fetch_MH.invokeExact(
        ctx.segment,
        algorithm?.let { arena.allocateUtf8String(it) } ?: MemorySegment.NULL,
        properties?.let { arena.allocateUtf8String(it) } ?: MemorySegment.NULL,
    ) as MemorySegment,
    ::EVP_MD
)

private val EVP_MD_CTX_new_MH: MethodHandle = Runtime.methodHandle(
    name = "EVP_MD_CTX_new",
    result = ValueLayout.ADDRESS,
)

actual fun CInteropScope.EVP_MD_CTX_new(): CPointer<EVP_MD_CTX>? {
    return CPointer(EVP_MD_CTX_new_MH.invokeExact() as MemorySegment, ::EVP_MD_CTX)
}

private val EVP_MD_get_size_MH: MethodHandle = Runtime.methodHandle(
    name = "EVP_MD_get_size",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun CInteropScope.EVP_MD_get_size(md: CPointer<EVP_MD>?): Int {
    return EVP_MD_get_size_MH.invokeExact(md.segment) as Int
}

private val EVP_DigestInit_MH: MethodHandle = Runtime.methodHandle(
    name = "EVP_DigestInit",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS)
)

actual fun CInteropScope.EVP_DigestInit(
    ctx: CPointer<EVP_MD_CTX>?,
    type: CPointer<EVP_MD>?,
): Int {
    return EVP_DigestInit_MH.invokeExact(ctx.segment, type.segment) as Int
}

private val EVP_DigestUpdate_MH: MethodHandle = Runtime.methodHandle(
    name = "EVP_DigestUpdate",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG)
)

actual fun CInteropScope.EVP_DigestUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    d: CPointer<*>?,
    cnt: CULong,
): Int {
    return EVP_DigestUpdate_MH.invokeExact(ctx.segment, d.segment, cnt.toLong()) as Int
}

private val EVP_DigestFinal_MH: MethodHandle = Runtime.methodHandle(
    name = "EVP_DigestFinal",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS)
)

actual fun CInteropScope.EVP_DigestFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    md: CPointer<CUByteVariable>?,
    s: CPointer<CUIntVariable>?,
): Int {
    return EVP_DigestFinal_MH.invokeExact(ctx.segment, md.segment, s.segment) as Int
}

private val EVP_MD_CTX_free_MH: MethodHandle = Runtime.methodHandle(
    name = "EVP_MD_CTX_free",
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun CInteropScope.EVP_MD_CTX_free(ctx: CPointer<EVP_MD_CTX>?) {
    EVP_MD_CTX_free_MH.invokeExact(ctx.segment)
}

private val EVP_MD_free_MH: MethodHandle = Runtime.methodHandle(
    name = "EVP_MD_free",
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun CInteropScope.EVP_MD_free(ctx: CPointer<EVP_MD>?) {
    EVP_MD_free_MH.invokeExact(ctx.segment)
}
