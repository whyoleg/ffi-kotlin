package dev.whyoleg.foreign.tooling.cx.compiler.internal

import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*
import kotlinx.cinterop.*

internal fun <T> indexTranslationUnit(
    action: CXIndexAction,
    translationUnit: CXTranslationUnit,
    indexer: CxIndexer<T>
): T {
    memScoped {
        useStableRef(indexer) { indexerRef ->
            val callbacks = alloc<IndexerCallbacks> {
                diagnostic = staticCFunction { clientData, diagnosticSet, _ ->
                    unwrapHandlerRef(clientData) { it.diagnostic(diagnosticSet!!) }
                }
                enteredMainFile = staticCFunction { clientData, mainFile, _ ->
                    unwrapHandlerRef(clientData) { it.enteredMainFile(mainFile!!) }
                    null //TODO
                }
                ppIncludedFile = staticCFunction { clientData, file ->
                    unwrapHandlerRef(clientData) { it.ppIncludedFile(file!!.pointed) }
                    null //TODO
                }
                indexDeclaration = staticCFunction { clientData, info ->
                    unwrapHandlerRef(clientData) { it.indexDeclaration(info!!.pointed) }
                }
            }
            val result = clang_indexTranslationUnit(
                action,
                indexerRef.asCPointer(),
                callbacks.ptr,
                sizeOf<IndexerCallbacks>().convert(),
                0u,
                translationUnit
            )
            if (result != 0) error("clang_indexTranslationUnit returned $result")
        }
    }
    return indexer.buildResult()
}

private inline fun unwrapHandlerRef(clientData: CXClientData?, block: (indexer: CxIndexer<*>) -> Unit) {
    val indexer = clientData!!.asStableRef<CxIndexer<*>>().get()

    indexer
        .runCatching(block)
        .recoverCatching(indexer::reportError)
        .recoverCatching(Throwable::printStackTrace)
}

private inline fun <T : Any, R> useStableRef(value: T, block: (ref: StableRef<T>) -> R): R {
    val ref = StableRef.create(value)
    try {
        return block(ref)
    } finally {
        ref.dispose()
    }
}
