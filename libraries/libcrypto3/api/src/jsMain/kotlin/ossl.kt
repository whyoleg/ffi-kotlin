@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

actual object OSSL_LIB_CTX_Type : COpaqueType<OSSL_LIB_CTX>(::OSSL_LIB_CTX)
actual class OSSL_LIB_CTX(memory: NativeMemory) : COpaque(memory) {
    override val type: OSSL_LIB_CTX_Type get() = OSSL_LIB_CTX_Type
}
