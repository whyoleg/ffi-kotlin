package dev.whyoleg.foreign.tooling.cx.compiler.internal

import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*

internal interface CxIndexer<T> {

    // clang callbacks
    fun diagnostic(diagnosticSet: CXDiagnosticSet)
    fun enteredMainFile(mainFile: CXFile)
    fun ppIncludedFile(fileInfo: CXIdxIncludedFileInfo)
    fun indexDeclaration(declarationInfo: CXIdxDeclInfo)

    // called if some callback handling fails
    fun reportError(cause: Throwable)

    // called when indexing completed
    fun buildResult(): T
}
