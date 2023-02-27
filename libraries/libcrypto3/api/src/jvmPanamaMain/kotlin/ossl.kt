@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*
import java.lang.foreign.*

actual class OSSL_LIB_CTX(segment: MemorySegment) : COpaque(segment)

actual object OSSL_LIB_CTX_Type : COpaqueType<OSSL_LIB_CTX>(::OSSL_LIB_CTX)
