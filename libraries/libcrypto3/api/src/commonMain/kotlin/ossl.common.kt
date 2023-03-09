@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

expect class OSSL_LIB_CTX : COpaque {
    companion object Type : COpaque.Type<OSSL_LIB_CTX>
}
