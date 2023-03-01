@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

actual object EVP_PKEY_CTX_Type : COpaqueType<EVP_PKEY_CTX>(::EVP_PKEY_CTX)
actual class EVP_PKEY_CTX(memory: NativeMemory) : COpaque(memory) {
    override val type: EVP_PKEY_CTX_Type get() = EVP_PKEY_CTX_Type
}

actual fun EVP_PKEY_CTX_new_from_name(
    libctx: CPointer<OSSL_LIB_CTX>?,
    name: CString?,
    propquery: CString?,
): CPointer<EVP_PKEY_CTX>? = CPointer(
    NativePointer(
        evppkeyctx.EVP_PKEY_CTX_new_from_name(libctx.nativePointer, name.nativePointer, propquery.nativePointer)
    ),
    EVP_PKEY_CTX_Type
)

actual fun EVP_PKEY_CTX_set_params(
    ctx: CPointer<EVP_PKEY_CTX>?,
    params: CPointer<OSSL_PARAM>?,
): Int {
    return evppkeyctx.EVP_PKEY_CTX_set_params(ctx.nativePointer, params.nativePointer)
}

actual fun EVP_PKEY_CTX_free(ctx: CPointer<EVP_PKEY_CTX>?) {
    evppkeyctx.EVP_PKEY_CTX_free(ctx.nativePointer)
}

@JsModule("ffi-libcrypto")
@JsNonModule
@JsName("Module")
private external object evppkeyctx {
    @JsName("_ffi_EVP_PKEY_CTX_new_from_name")
    fun EVP_PKEY_CTX_new_from_name(libctx: Int, name: Int, propquery: Int): Int

    @JsName("_ffi_EVP_PKEY_CTX_set_params")
    fun EVP_PKEY_CTX_set_params(ctx: Int, params: Int): Int

    @JsName("_ffi_EVP_PKEY_CTX_free")
    fun EVP_PKEY_CTX_free(ctx: Int)
}
