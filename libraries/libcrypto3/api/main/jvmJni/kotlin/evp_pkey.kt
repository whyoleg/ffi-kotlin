@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

actual object EVP_PKEY_Type : COpaqueType<EVP_PKEY>(::EVP_PKEY)
actual class EVP_PKEY(memory: NativeMemory) : COpaque(memory) {
    override val type: EVP_PKEY_Type get() = EVP_PKEY_Type
}

actual fun EVP_PKEY_keygen_init(ctx: CPointer<EVP_PKEY_CTX>?): Int {
    return evppkey.EVP_PKEY_keygen_init(ctx.nativePointer)
}

actual fun EVP_PKEY_generate(
    ctx: CPointer<EVP_PKEY_CTX>?,
    ppkey: CPointer<CPointerVariable<EVP_PKEY>>?,
): Int {
    return evppkey.EVP_PKEY_generate(ctx.nativePointer, ppkey.nativePointer)
}

actual fun EVP_PKEY_up_ref(pkey: CPointer<EVP_PKEY>?): Int {
    return evppkey.EVP_PKEY_up_ref(pkey.nativePointer)
}

actual fun EVP_PKEY_free(pkey: CPointer<EVP_PKEY>?) {
    evppkey.EVP_PKEY_free(pkey.nativePointer)
}

private object evppkey {
    init {
        JNI
    }

    @JvmStatic
    external fun EVP_PKEY_keygen_init(ctx: Long): Int

    @JvmStatic
    external fun EVP_PKEY_generate(ctx: Long, ppkey: Long): Int

    @JvmStatic
    external fun EVP_PKEY_up_ref(pkey: Long): Int

    @JvmStatic
    external fun EVP_PKEY_free(pkey: Long)
}
