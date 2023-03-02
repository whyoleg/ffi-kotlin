@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*
import java.lang.foreign.*
import java.lang.invoke.*

private val EVP_MAC_fetch_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_fetch",
    result = ValueLayout.ADDRESS,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS)
)

actual fun EVP_MAC_fetch(
    libctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: CString?,
    properties: CString?,
): CPointer<EVP_MAC>? = nativeCPointer(
    EVP_MAC_Type,
    EVP_MAC_fetch_MH.invokeExact(
        libctx.nativeAddress,
        algorithm.nativeAddress,
        properties.nativeAddress,
    ) as MemorySegment
)

private val EVP_MAC_CTX_new_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_CTX_new",
    result = ValueLayout.ADDRESS,
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_MAC_CTX_new(
    mac: CPointer<EVP_MAC>?,
): CPointer<EVP_MAC_CTX>? {
    return nativeCPointer(EVP_MAC_CTX_Type, EVP_MAC_CTX_new_MH.invokeExact(mac.nativeAddress) as MemorySegment)
}

private val EVP_MAC_init_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_init",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, OSSL_PARAM_layout)
)

actual fun EVP_MAC_init(
    ctx: CPointer<EVP_MAC_CTX>?,
    key: CPointer<UByteVariable>?,
    keylen: PlatformDependentUInt,
    params: CPointer<OSSL_PARAM>?,
): Int {
    return EVP_MAC_init_MH.invokeExact(
        ctx.nativeAddress,
        key.nativeAddress,
        keylen.toLong(),
        params.nativeAddress
    ) as Int
}

private val EVP_MAC_CTX_get_mac_size_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_CTX_get_mac_size",
    result = ValueLayout.JAVA_LONG,
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_MAC_CTX_get_mac_size(
    ctx: CPointer<EVP_MAC_CTX>?,
): PlatformDependentUInt {
    return (EVP_MAC_CTX_get_mac_size_MH.invokeExact(ctx.nativeAddress) as Long).toULong()
}

private val EVP_MAC_update_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_update",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG)
)

actual fun EVP_MAC_update(
    ctx: CPointer<EVP_MAC_CTX>?,
    data: CPointer<UByteVariable>?,
    datalen: PlatformDependentUInt,
): Int {
    return EVP_MAC_update_MH.invokeExact(
        ctx.nativeAddress,
        data.nativeAddress,
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
    out: CPointer<UByteVariable>?,
    outl: CPointer<PlatformDependentUIntVariable>?,
    outsize: PlatformDependentUInt,
): Int {
    return EVP_MAC_final_MH.invokeExact(
        ctx.nativeAddress,
        out.nativeAddress,
        outl.nativeAddress,
        outsize.toLong(),
    ) as Int
}

private val EVP_MAC_CTX_free_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_CTX_free",
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_MAC_CTX_free(ctx: CPointer<EVP_MAC_CTX>?) {
    EVP_MAC_CTX_free_MH.invokeExact(ctx.nativeAddress)
}

private val EVP_MAC_free_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_free",
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_MAC_free(ctx: CPointer<EVP_MAC>?) {
    EVP_MAC_free_MH.invokeExact(ctx.nativeAddress)
}
