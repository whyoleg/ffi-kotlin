@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*
import java.lang.foreign.*
import java.lang.invoke.*

private val EVP_PKEY_keygen_init: MethodHandle = FFI.methodHandle(
    name = "EVP_PKEY_keygen_init",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_PKEY_keygen_init(ctx: CPointer<EVP_PKEY_CTX>?): Int {
    return EVP_PKEY_keygen_init.invokeExact(
        ctx.nativeAddress
    ) as Int
}

private val EVP_PKEY_generate: MethodHandle = FFI.methodHandle(
    name = "EVP_PKEY_generate",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS)
)

actual fun EVP_PKEY_generate(
    ctx: CPointer<EVP_PKEY_CTX>?,
    ppkey: CPointer<CPointerVariable<EVP_PKEY>>?,
): Int {
    return EVP_PKEY_generate.invokeExact(
        ctx.nativeAddress,
        ppkey.nativeAddress
    ) as Int
}

private val EVP_PKEY_up_ref: MethodHandle = FFI.methodHandle(
    name = "EVP_PKEY_up_ref",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_PKEY_up_ref(pkey: CPointer<EVP_PKEY>?): Int {
    return EVP_PKEY_up_ref.invokeExact(
        pkey.nativeAddress
    ) as Int
}

private val EVP_PKEY_free: MethodHandle = FFI.methodHandle(
    name = "EVP_PKEY_free",
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_PKEY_free(pkey: CPointer<EVP_PKEY>?) {
    EVP_PKEY_free.invokeExact(pkey.nativeAddress)
}
