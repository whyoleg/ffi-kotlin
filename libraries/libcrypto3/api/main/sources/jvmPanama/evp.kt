@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

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

actual class EVP_MAC(segment: MemorySegment) : COpaque(segment)
actual class EVP_MAC_CTX(segment: MemorySegment) : COpaque(segment)

private val EVP_MD_fetch_MH: MethodHandle = Runtime.methodHandle(
    name = "EVP_MD_fetch",
    result = ValueLayout.ADDRESS,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS)
)

actual fun CInteropScope.EVP_MD_fetch(
    ctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: String?,
    properties: String?,
): CPointer<EVP_MD>? = CPointer(
    EVP_MD_fetch_MH.invokeExact(
        ctx.segment,
        algorithm?.let(::alloc).segment,
        properties?.let(::alloc).segment,
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

private val EVP_MAC_fetch_MH: MethodHandle = Runtime.methodHandle(
    name = "EVP_MAC_fetch",
    result = ValueLayout.ADDRESS,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS)
)

actual fun CInteropScope.EVP_MAC_fetch(
    libctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: String?,
    properties: String?,
): CPointer<EVP_MAC>? = CPointer(
    EVP_MAC_fetch_MH.invokeExact(
        libctx.segment,
        algorithm?.let(::alloc).segment,
        properties?.let(::alloc).segment,
    ) as MemorySegment,
    ::EVP_MAC
)

private val EVP_MAC_CTX_new_MH: MethodHandle = Runtime.methodHandle(
    name = "EVP_MAC_CTX_new",
    result = ValueLayout.ADDRESS,
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun CInteropScope.EVP_MAC_CTX_new(
    mac: CPointer<EVP_MAC>?,
): CPointer<EVP_MAC_CTX>? {
    return CPointer(EVP_MAC_CTX_new_MH.invokeExact(mac.segment) as MemorySegment, ::EVP_MAC_CTX)
}

private val EVP_MAC_init_MH: MethodHandle = Runtime.methodHandle(
    name = "EVP_MAC_init",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, OSSL_PARAM_Type.layout)
)

actual fun CInteropScope.EVP_MAC_init(
    ctx: CPointer<EVP_MAC_CTX>?,
    key: CPointer<CUByteVariable>?,
    keylen: CULong,
    params: CPointer<OSSL_PARAM>?,
): Int {
    return EVP_MAC_init_MH.invokeExact(
        ctx.segment,
        key.segment,
        keylen.toLong(),
        params.segment
    ) as Int
}

private val EVP_MAC_CTX_get_mac_size_MH: MethodHandle = Runtime.methodHandle(
    name = "EVP_MAC_CTX_get_mac_size",
    result = ValueLayout.JAVA_LONG,
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun CInteropScope.EVP_MAC_CTX_get_mac_size(
    ctx: CPointer<EVP_MAC_CTX>?,
): CULong {
    return (EVP_MAC_CTX_get_mac_size_MH.invokeExact(ctx.segment) as Long).toULong()
}

private val EVP_MAC_update_MH: MethodHandle = Runtime.methodHandle(
    name = "EVP_MAC_update",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG)
)

actual fun CInteropScope.EVP_MAC_update(
    ctx: CPointer<EVP_MAC_CTX>?,
    data: CPointer<CUByteVariable>?,
    datalen: CULong,
): Int {
    return EVP_MAC_update_MH.invokeExact(
        ctx.segment,
        data.segment,
        datalen.toLong(),
    ) as Int
}

private val EVP_MAC_final_MH: MethodHandle = Runtime.methodHandle(
    name = "EVP_MAC_final",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG)
)

actual fun CInteropScope.EVP_MAC_final(
    ctx: CPointer<EVP_MAC_CTX>?,
    out: CPointer<CUByteVariable>?,
    outl: CPointer<CULongVariable>?,
    outsize: CULong,
): Int {
    return EVP_MAC_final_MH.invokeExact(
        ctx.segment,
        out.segment,
        outl.segment,
        outsize.toLong(),
    ) as Int
}

private val EVP_MAC_CTX_free_MH: MethodHandle = Runtime.methodHandle(
    name = "EVP_MAC_CTX_free",
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_MAC_CTX_free(ctx: CPointer<EVP_MAC_CTX>?) {
    EVP_MAC_CTX_free_MH.invokeExact(ctx.segment)
}

private val EVP_MAC_free_MH: MethodHandle = Runtime.methodHandle(
    name = "EVP_MAC_free",
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_MAC_free(ctx: CPointer<EVP_MAC>?) {
    EVP_MAC_free_MH.invokeExact(ctx.segment)
}

private val OSSL_PARAM_construct_utf8_string_MH: MethodHandle = Runtime.methodHandle(
    name = "OSSL_PARAM_construct_utf8_string",
    result = OSSL_PARAM_Type.layout,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG)
)

actual fun CInteropScope.OSSL_PARAM_construct_utf8_string(
    key: String?,
    buf: CString?,
    bsize: CULong,
): CValue<OSSL_PARAM> = cValue(
    OSSL_PARAM_Type,
    OSSL_PARAM_construct_utf8_string_MH.invokeExact(
        arena as SegmentAllocator,
        key?.let(::alloc).segment,
        buf.segment,
        bsize.toLong()
    ) as MemorySegment
)

private val OSSL_PARAM_construct_end_MH: MethodHandle = Runtime.methodHandle(
    name = "OSSL_PARAM_construct_end",
    result = OSSL_PARAM_Type.layout
)

actual fun CInteropScope.OSSL_PARAM_construct_end(): CValue<OSSL_PARAM> = cValue(
    OSSL_PARAM_Type,
    OSSL_PARAM_construct_end_MH.invokeExact(
        arena as SegmentAllocator
    ) as MemorySegment
)
