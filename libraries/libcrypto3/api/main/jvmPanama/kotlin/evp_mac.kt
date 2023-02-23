@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*
import java.lang.foreign.*
import java.lang.invoke.*

actual class EVP_MAC(segment: MemorySegment) : COpaque(segment)
actual object EVP_MAC_Type : COpaqueType<EVP_MAC>(::EVP_MAC)
actual class EVP_MAC_CTX(segment: MemorySegment) : COpaque(segment)
actual object EVP_MAC_CTX_Type : COpaqueType<EVP_MAC_CTX>(::EVP_MAC_CTX)

private val EVP_MAC_fetch_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_fetch",
    result = ValueLayout.ADDRESS,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS)
)

actual fun EVP_MAC_fetch(
    libctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: CString?,
    properties: CString?,
): CPointer<EVP_MAC>? = CPointer(
    EVP_MAC_fetch_MH.invokeExact(
        libctx.segment,
        algorithm.segment,
        properties.segment,
    ) as MemorySegment,
    EVP_MAC_Type
)

private val EVP_MAC_CTX_new_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_CTX_new",
    result = ValueLayout.ADDRESS,
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_MAC_CTX_new(
    mac: CPointer<EVP_MAC>?,
): CPointer<EVP_MAC_CTX>? {
    return CPointer(EVP_MAC_CTX_new_MH.invokeExact(mac.segment) as MemorySegment, EVP_MAC_CTX_Type)
}

private val EVP_MAC_init_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_init",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, OSSL_PARAM_Type.layout)
)

actual fun EVP_MAC_init(
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

private val EVP_MAC_CTX_get_mac_size_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_CTX_get_mac_size",
    result = ValueLayout.JAVA_LONG,
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_MAC_CTX_get_mac_size(
    ctx: CPointer<EVP_MAC_CTX>?,
): CULong {
    return (EVP_MAC_CTX_get_mac_size_MH.invokeExact(ctx.segment) as Long).toULong()
}

private val EVP_MAC_update_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_update",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG)
)

actual fun EVP_MAC_update(
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

private val EVP_MAC_final_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_final",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG)
)

actual fun EVP_MAC_final(
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

private val EVP_MAC_CTX_free_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_CTX_free",
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_MAC_CTX_free(ctx: CPointer<EVP_MAC_CTX>?) {
    EVP_MAC_CTX_free_MH.invokeExact(ctx.segment)
}

private val EVP_MAC_free_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_free",
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_MAC_free(ctx: CPointer<EVP_MAC>?) {
    EVP_MAC_free_MH.invokeExact(ctx.segment)
}
