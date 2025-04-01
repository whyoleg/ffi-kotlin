package dev.whyoleg.foreign.tool.clang

import dev.whyoleg.foreign.tool.libclang.*
import kotlinx.cinterop.*

internal fun CValue<CXString>.useString(): String? = try {
    clang_getCString(this)?.toKString()
} finally {
    clang_disposeString(this)
}
