package dev.whyoleg.foreign.clang.compiler

import dev.whyoleg.foreign.clang.compiler.libclang.*
import kotlinx.cinterop.*

internal fun CValue<CXString>.useString(): String? = try {
    clang_getCString(this)?.toKString()
} finally {
    clang_disposeString(this)
}
