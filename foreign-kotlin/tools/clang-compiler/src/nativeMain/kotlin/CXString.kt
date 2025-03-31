package dev.whyoleg.foreign.tool.clang.compiler

import dev.whyoleg.foreign.tool.clang.compiler.libclang.*
import kotlinx.cinterop.*

internal fun CValue<CXString>.useString(): String? = try {
    clang_getCString(this)?.toKString()
} finally {
    clang_disposeString(this)
}
