package dev.whyoleg.foreign.cx.index.cli.internal

import dev.whyoleg.foreign.cx.index.clang.*

inline fun <R> useIndex(
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
