@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*
import java.lang.foreign.*
import java.lang.invoke.*

actual class EVP_PKEY_CTX(segment: MemorySegment) : COpaque(segment)
actual object EVP_PKEY_CTX_Type : COpaqueType<EVP_PKEY_CTX>(::EVP_PKEY_CTX)

private val EVP_PKEY_CTX_new_from_name: MethodHandle = FFI.methodHandle(
    name = "EVP_PKEY_CTX_new_from_name",
    result = ValueLayout.ADDRESS,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS)
)

actual fun EVP_PKEY_CTX_new_from_name(
    libctx: CPointer<OSSL_LIB_CTX>?,
    name: CString?,
    propquery: CString?,
): CPointer<EVP_PKEY_CTX>? = CPointer(
    EVP_PKEY_CTX_new_from_name.invokeExact(
        libctx.segment,
        name.segment,
        propquery.segment
    ) as MemorySegment,
    EVP_PKEY_CTX_Type
)

private val EVP_PKEY_CTX_set_params: MethodHandle = FFI.methodHandle(
    name = "EVP_PKEY_CTX_set_params",
    result = ValueLayout.JAVA_INT,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS)
)

actual fun EVP_PKEY_CTX_set_params(
    ctx: CPointer<EVP_PKEY_CTX>?,
    params: CPointer<OSSL_PARAM>?,
): Int {
    return EVP_PKEY_CTX_set_params.invokeExact(
        ctx.segment,
        params.segment
    ) as Int
}

private val EVP_PKEY_CTX_free: MethodHandle = FFI.methodHandle(
    name = "EVP_PKEY_CTX_free",
    args = arrayOf(ValueLayout.ADDRESS)
)

actual fun EVP_PKEY_CTX_free(ctx: CPointer<EVP_PKEY_CTX>?) {
    EVP_PKEY_CTX_free.invokeExact(ctx.segment)
}
