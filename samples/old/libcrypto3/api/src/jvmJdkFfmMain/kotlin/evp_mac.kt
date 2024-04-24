@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.invoke.*
import dev.whyoleg.foreign.platform.*
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
): CPointer<EVP_MAC>? = libCrypto3ImplicitScope.unsafe {
    val address = EVP_MAC_fetch_MH.invokeExact(
        libctx.address,
        algorithm.address,
        properties.address,
    ) as MemorySegment
    CPointer(EVP_MAC, address)
}

private val EVP_MAC_CTX_new_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_CTX_new",
    result = ValueLayout.ADDRESS,
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_MAC_CTX_new(
    mac: CPointer<EVP_MAC>?,
): CPointer<EVP_MAC_CTX>? = libCrypto3ImplicitScope.unsafe {
    CPointer(EVP_MAC_CTX, EVP_MAC_CTX_new_MH.invokeExact(mac.address) as MemorySegment)
}

private val EVP_MAC_init_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_init",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, OSSL_PARAM_layout)
)

actual fun EVP_MAC_init(
    ctx: CPointer<EVP_MAC_CTX>?,
    key: CPointer<UByte>?,
    keylen: PlatformUInt,
    params: CArrayPointer<OSSL_PARAM>?,
): Int = libCrypto3ImplicitScope.unsafe {
    EVP_MAC_init_MH.invokeExact(
        ctx.address,
        key.address,
        keylen.toLong(),
        params.address
    ) as Int
}

private val EVP_MAC_CTX_get_mac_size_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_CTX_get_mac_size",
    result = ValueLayout.JAVA_LONG,
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_MAC_CTX_get_mac_size(
    ctx: CPointer<EVP_MAC_CTX>?,
): PlatformUInt = libCrypto3ImplicitScope.unsafe {
    (EVP_MAC_CTX_get_mac_size_MH.invokeExact(ctx.address) as Long).toULong()
}

private val EVP_MAC_update_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_update",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG)
)

actual fun EVP_MAC_update(
    ctx: CPointer<EVP_MAC_CTX>?,
    data: CPointer<UByte>?,
    datalen: PlatformUInt,
): Int = libCrypto3ImplicitScope.unsafe {
    EVP_MAC_update_MH.invokeExact(
        ctx.address,
        data.address,
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
    out: CPointer<UByte>?,
    outl: CPointer<PlatformUInt>?,
    outsize: PlatformUInt,
): Int = libCrypto3ImplicitScope.unsafe {
    EVP_MAC_final_MH.invokeExact(
        ctx.address,
        out.address,
        outl.address,
        outsize.toLong(),
    ) as Int
}

private val EVP_MAC_CTX_free_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_CTX_free",
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_MAC_CTX_free(ctx: CPointer<EVP_MAC_CTX>?): Unit = libCrypto3ImplicitScope.unsafe {
    EVP_MAC_CTX_free_MH.invokeExact(ctx.address)
}

private val EVP_MAC_free_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_free",
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_MAC_free(ctx: CPointer<EVP_MAC>?): Unit = libCrypto3ImplicitScope.unsafe {
    EVP_MAC_free_MH.invokeExact(ctx.address)
}
