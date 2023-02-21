@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*
import java.lang.foreign.*

actual class OSSL_LIB_CTX(segment: MemorySegment) : COpaque(segment)
