@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.invoke.*
import java.lang.foreign.*
import java.lang.invoke.*

private val EVP_PKEY_CTX_new_from_name: MethodHandle = FFI.methodHandle(
    name = "EVP_PKEY_CTX_new_from_name",
    result = ValueLayout.ADDRESS,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS)
)

actual fun EVP_PKEY_CTX_new_from_name(
    libctx: CPointer<OSSL_LIB_CTX>?,
    name: CString?,
    propquery: CString?,
): CPointer<EVP_PKEY_CTX>? = libCrypto3ImplicitScope.unsafe {
    val address = EVP_PKEY_CTX_new_from_name.invokeExact(
        libctx.address,
        name.address,
        propquery.address
    ) as MemorySegment
    CPointer(EVP_PKEY_CTX, address)
}

private val EVP_PKEY_CTX_set_params: MethodHandle = FFI.methodHandle(
    name = "EVP_PKEY_CTX_set_params",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS)
)

actual fun EVP_PKEY_CTX_set_params(
    ctx: CPointer<EVP_PKEY_CTX>?,
    params: CArrayPointer<OSSL_PARAM>?,
): Int = libCrypto3ImplicitScope.unsafe {
    EVP_PKEY_CTX_set_params.invokeExact(
        ctx.address,
        params.address
    ) as Int
}

private val EVP_PKEY_CTX_free: MethodHandle = FFI.methodHandle(
    name = "EVP_PKEY_CTX_free",
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_PKEY_CTX_free(ctx: CPointer<EVP_PKEY_CTX>?): Unit = libCrypto3ImplicitScope.unsafe {
    EVP_PKEY_CTX_free.invokeExact(ctx.address)
}
