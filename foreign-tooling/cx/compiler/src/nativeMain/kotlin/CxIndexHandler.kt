package dev.whyoleg.foreign.cx.index.generator

import dev.whyoleg.foreign.cx.index.clang.*
import kotlinx.cinterop.*

internal abstract class CxIndexHandler {
    abstract fun diagnostic(diagnosticSet: CXDiagnosticSet)
    abstract fun enteredMainFile(mainFile: CXFile)
    abstract fun ppIncludedFile(fileInfo: CXIdxIncludedFileInfo)
    abstract fun indexDeclaration(declarationInfo: CXIdxDeclInfo)

    abstract fun unhandledError(cause: Throwable)
}

internal inline fun <T> useIndexHandler(
    handler: CxIndexHandler,
    block: (
        handlerPointer: COpaquePointer,
        callbacksPointer: CPointer<IndexerCallbacks>
    ) -> T
): T = memScoped {
    useStableRef(handler) { handlerRef ->
        block(
            handlerRef.asCPointer(),
            alloc<IndexerCallbacks> {
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
            }.ptr
        )
    }
}

private inline fun unwrapHandlerRef(clientData: CXClientData?, block: (runner: CxIndexHandler) -> Unit) {
    val handler = clientData!!.asStableRef<CxIndexHandler>().get()

    handler
        .runCatching(block)
        .recoverCatching(handler::unhandledError)
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
