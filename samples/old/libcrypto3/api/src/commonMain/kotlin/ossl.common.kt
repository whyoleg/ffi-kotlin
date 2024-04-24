@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*

class OSSL_LIB_CTX private constructor() : COpaque() {
    companion object Type : CType.Opaque<OSSL_LIB_CTX>(OSSL_LIB_CTX())
}
