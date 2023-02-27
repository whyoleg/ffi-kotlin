@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
    "ACTUAL_TYPE_ALIAS_NOT_TO_CLASS",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

actual typealias OSSL_LIB_CTX = dev.whyoleg.ffi.libcrypto3.cinterop.OSSL_LIB_CTX

actual object OSSL_LIB_CTX_Type : COpaqueType<OSSL_LIB_CTX>()
