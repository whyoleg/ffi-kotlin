@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

expect class OSSL_LIB_CTX : COpaque
expect object OSSL_LIB_CTX_Type : COpaqueType<OSSL_LIB_CTX>
