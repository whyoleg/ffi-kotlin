package dev.whyoleg.foreign.tooling.clang

import dev.whyoleg.foreign.tooling.clang.libclang.*
import kotlinx.cinterop.*

internal fun CValue<CXString>.useString(): String? = try {
    clang_getCString(this)?.toKString()
} finally {
    clang_disposeString(this)
}
