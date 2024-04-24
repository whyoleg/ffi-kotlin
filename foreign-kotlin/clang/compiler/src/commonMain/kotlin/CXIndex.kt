package dev.whyoleg.foreign.clang.compiler

import dev.whyoleg.foreign.clang.compiler.libclang.*

internal inline fun <R> useIndex(
    excludeDeclarationsFromPCH: Boolean = false, // disables visitChildren to visit declarations from imported translation units
    block: (index: CXIndex) -> R,
): R {
    val index = checkNotNull(
        clang_createIndex(
            excludeDeclarationsFromPCH = if (excludeDeclarationsFromPCH) 1 else 0,
            displayDiagnostics = 0
        )
    )
    try {
        return block(index)
    } finally {
        clang_disposeIndex(index)
    }
}
