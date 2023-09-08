package dev.whyoleg.foreign.tooling.cx.compiler.internal

import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*

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

internal inline fun <T> useIndexAction(index: CXIndex, block: (action: CXIndexAction) -> T): T {
    val action = checkNotNull(clang_IndexAction_create(index)) { "IndexAction is null" }
    try {
        return block(action)
    } finally {
        clang_IndexAction_dispose(action)
    }
}
