@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import kotlinx.cinterop.internal.*

actual fun EVP_PKEY_keygen_init(ctx: CPointer<EVP_PKEY_CTX>?): Int = libCrypto3ImplicitScope.unsafe {
    ffi_EVP_PKEY_keygen_init(ctx.address)
}

actual fun EVP_PKEY_generate(
    ctx: CPointer<EVP_PKEY_CTX>?,
    ppkey: CPointer<CPointer<EVP_PKEY>>?,
): Int = libCrypto3ImplicitScope.unsafe {
    ffi_EVP_PKEY_generate(ctx.address, ppkey.address)
}

actual fun EVP_PKEY_up_ref(pkey: CPointer<EVP_PKEY>?): Int = libCrypto3ImplicitScope.unsafe {
    ffi_EVP_PKEY_up_ref(pkey.address)
}

actual fun EVP_PKEY_free(pkey: CPointer<EVP_PKEY>?) = libCrypto3ImplicitScope.unsafe {
    ffi_EVP_PKEY_free(pkey.address)
}


@CCall("ffi_EVP_PKEY_keygen_init")
private external fun ffi_EVP_PKEY_keygen_init(ctx: Long): Int

@CCall("ffi_EVP_PKEY_generate")
private external fun ffi_EVP_PKEY_generate(ctx: Long, ppkey: Long): Int

@CCall("ffi_EVP_PKEY_up_ref")
private external fun ffi_EVP_PKEY_up_ref(pkey: Long): Int

@CCall("ffi_EVP_PKEY_free")
private external fun ffi_EVP_PKEY_free(pkey: Long)