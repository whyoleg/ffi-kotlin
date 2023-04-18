@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.invoke.*
import java.lang.foreign.*
import java.lang.invoke.*

private val EVP_PKEY_keygen_init: MethodHandle = FFI.methodHandle(
    name = "EVP_PKEY_keygen_init",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_PKEY_keygen_init(ctx: CPointer<EVP_PKEY_CTX>?): Int = libCrypto3ImplicitScope.unsafe {
    EVP_PKEY_keygen_init.invokeExact(
        ctx.address
    ) as Int
}

private val EVP_PKEY_generate: MethodHandle = FFI.methodHandle(
    name = "EVP_PKEY_generate",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS)
)

actual fun EVP_PKEY_generate(
    ctx: CPointer<EVP_PKEY_CTX>?,
    ppkey: CPointer<CPointer<EVP_PKEY>>?,
): Int = libCrypto3ImplicitScope.unsafe {
    EVP_PKEY_generate.invokeExact(
        ctx.address,
        ppkey.address
    ) as Int
}

private val EVP_PKEY_up_ref: MethodHandle = FFI.methodHandle(
    name = "EVP_PKEY_up_ref",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_PKEY_up_ref(pkey: CPointer<EVP_PKEY>?): Int = libCrypto3ImplicitScope.unsafe {
    EVP_PKEY_up_ref.invokeExact(
        pkey.address
    ) as Int
}

private val EVP_PKEY_free: MethodHandle = FFI.methodHandle(
    name = "EVP_PKEY_free",
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_PKEY_free(pkey: CPointer<EVP_PKEY>?): Unit = libCrypto3ImplicitScope.unsafe {
    EVP_PKEY_free.invokeExact(pkey.address)
}
