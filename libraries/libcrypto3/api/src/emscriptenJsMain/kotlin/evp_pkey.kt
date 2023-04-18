@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*

actual fun EVP_PKEY_keygen_init(ctx: CPointer<EVP_PKEY_CTX>?): Int = libCrypto3ImplicitScope.unsafe {
    evppkey.EVP_PKEY_keygen_init(ctx.address)
}

actual fun EVP_PKEY_generate(
    ctx: CPointer<EVP_PKEY_CTX>?,
    ppkey: CPointer<CPointer<EVP_PKEY>>?,
): Int = libCrypto3ImplicitScope.unsafe {
    evppkey.EVP_PKEY_generate(ctx.address, ppkey.address)
}

actual fun EVP_PKEY_up_ref(pkey: CPointer<EVP_PKEY>?): Int = libCrypto3ImplicitScope.unsafe {
    evppkey.EVP_PKEY_up_ref(pkey.address)
}

actual fun EVP_PKEY_free(pkey: CPointer<EVP_PKEY>?) = libCrypto3ImplicitScope.unsafe {
    evppkey.EVP_PKEY_free(pkey.address)
}

@JsModule("foreign-crypto-wasm")
@JsNonModule
@JsName("Module")
private external object evppkey {
    @JsName("_ffi_EVP_PKEY_keygen_init")
    fun EVP_PKEY_keygen_init(ctx: Int): Int

    @JsName("_ffi_EVP_PKEY_generate")
    fun EVP_PKEY_generate(ctx: Int, ppkey: Int): Int

    @JsName("_ffi_EVP_PKEY_up_ref")
    fun EVP_PKEY_up_ref(pkey: Int): Int

    @JsName("_ffi_EVP_PKEY_free")
    fun EVP_PKEY_free(pkey: Int)
}
