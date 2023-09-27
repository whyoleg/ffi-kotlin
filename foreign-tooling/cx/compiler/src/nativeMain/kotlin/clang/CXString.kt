package dev.whyoleg.foreign.tooling.cx.compiler.clang

import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*
import kotlinx.cinterop.*

internal fun CValue<CXString>.useString(): String? = try {
    clang_getCString(this)?.toKString()
} finally {
    clang_disposeString(this)
}
