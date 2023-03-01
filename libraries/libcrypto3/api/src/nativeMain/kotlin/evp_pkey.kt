@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
    "ACTUAL_TYPE_ALIAS_NOT_TO_CLASS",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

actual typealias EVP_PKEY = dev.whyoleg.ffi.libcrypto3.cinterop.EVP_PKEY

actual object EVP_PKEY_Type : COpaqueType<EVP_PKEY>()

actual fun EVP_PKEY_keygen_init(ctx: CPointer<EVP_PKEY_CTX>?): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_PKEY_keygen_init(ctx)
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual fun EVP_PKEY_generate(
    ctx: CPointer<EVP_PKEY_CTX>?,
    ppkey: CPointer<CPointerVariable<EVP_PKEY>>?,
): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_PKEY_generate(ctx, ppkey)
}

actual fun EVP_PKEY_up_ref(pkey: CPointer<EVP_PKEY>?): Int {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_PKEY_up_ref(pkey)
}

actual fun EVP_PKEY_free(pkey: CPointer<EVP_PKEY>?) {
    return dev.whyoleg.ffi.libcrypto3.cinterop.EVP_PKEY_free(pkey)
}
