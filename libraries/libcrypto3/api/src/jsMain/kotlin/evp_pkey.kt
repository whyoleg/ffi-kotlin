@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

actual fun EVP_PKEY_keygen_init(ctx: CPointer<EVP_PKEY_CTX>?): Int {
    return evppkey.EVP_PKEY_keygen_init(ctx.nativeAddress)
}

actual fun EVP_PKEY_generate(
    ctx: CPointer<EVP_PKEY_CTX>?,
    ppkey: CPointer<CPointerVariable<EVP_PKEY>>?,
): Int {
    return evppkey.EVP_PKEY_generate(ctx.nativeAddress, ppkey.nativeAddress)
}

actual fun EVP_PKEY_up_ref(pkey: CPointer<EVP_PKEY>?): Int {
    return evppkey.EVP_PKEY_up_ref(pkey.nativeAddress)
}

actual fun EVP_PKEY_free(pkey: CPointer<EVP_PKEY>?) {
    evppkey.EVP_PKEY_free(pkey.nativeAddress)
}

@JsModule("ffi-libcrypto")
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
