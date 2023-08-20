package dev.whyoleg.foreign.index.cx.generator

import dev.whyoleg.foreign.index.cx.clang.*
import dev.whyoleg.foreign.index.cx.generator.internal.*
import kotlinx.cinterop.*

internal class CxIndexRunner(
    private val builder: CxIndexBuilder
) {
    private val reportedErrors = ArrayDeque<Throwable>()

    private fun diagnostic(diagnosticSet: CXDiagnosticSet) {
        repeat(clang_getNumDiagnosticsInSet(diagnosticSet).toInt()) { i ->
            val diagnostic = clang_getDiagnosticInSet(diagnosticSet, i.toUInt()) ?: return@repeat
            try {
                val severity = clang_getDiagnosticSeverity(diagnostic)
                val format = clang_formatDiagnostic(diagnostic, clang_defaultDiagnosticDisplayOptions()).useString()
//                    val location = clang_getDiagnosticLocation(diagnostic)
                //println("D: $severity | $format")
            } finally {
                clang_disposeDiagnostic(diagnostic)
            }
        }
    }

    private fun enteredMainFile(mainFile: CXFile) {
        println("Header: ${clang_getFileName(mainFile).useString()}")
    }

    private fun ppIncludedFile(fileInfo: CXIdxIncludedFileInfo) {
        val includeName = fileInfo.filename?.toKString()
        val fileName = clang_getFileName(fileInfo.file).useString()

        if (includeName != null && fileName != null) {
            builder.include(includeName, fileName)
        } else {
            reportError(IllegalStateException("ppIncludedFile | $includeName | $fileName"))
        }
    }

    private fun indexDeclaration(declarationInfo: CXIdxDeclInfo) {
        val entityInfo = declarationInfo.entityInfo!!.pointed
        val cursor = declarationInfo.cursor.readValue()
        when (val kind = entityInfo.kind) {
            CXIdxEntityKind.CXIdxEntity_Function     -> builder.function(cursor)
            CXIdxEntityKind.CXIdxEntity_Struct,
            CXIdxEntityKind.CXIdxEntity_Union        -> builder.record(cursor)
            CXIdxEntityKind.CXIdxEntity_Typedef      -> builder.typedef(cursor)
            CXIdxEntityKind.CXIdxEntity_Enum         -> builder.enum(cursor)
            CXIdxEntityKind.CXIdxEntity_Field,
            CXIdxEntityKind.CXIdxEntity_EnumConstant -> {
            } //TODO: ignore?
            CXIdxEntityKind.CXIdxEntity_Unexposed    -> {}//TODO("CXIdxEntity_Unexposed")
            CXIdxEntityKind.CXIdxEntity_Variable     -> {}//TODO("CXIdxEntity_Variable")

            CXIdxEntityKind.CXIdxEntity_ObjCClass,
            CXIdxEntityKind.CXIdxEntity_ObjCProtocol,
            CXIdxEntityKind.CXIdxEntity_ObjCCategory,
            CXIdxEntityKind.CXIdxEntity_ObjCInstanceMethod,
            CXIdxEntityKind.CXIdxEntity_ObjCClassMethod,
            CXIdxEntityKind.CXIdxEntity_ObjCProperty,
            CXIdxEntityKind.CXIdxEntity_ObjCIvar     -> println("SKIP OBJ-C DECLARATION")
            CXIdxEntityKind.CXIdxEntity_CXXClass,
            CXIdxEntityKind.CXIdxEntity_CXXNamespace,
            CXIdxEntityKind.CXIdxEntity_CXXNamespaceAlias,
            CXIdxEntityKind.CXIdxEntity_CXXStaticVariable,
            CXIdxEntityKind.CXIdxEntity_CXXStaticMethod,
            CXIdxEntityKind.CXIdxEntity_CXXInstanceMethod,
            CXIdxEntityKind.CXIdxEntity_CXXConstructor,
            CXIdxEntityKind.CXIdxEntity_CXXDestructor,
            CXIdxEntityKind.CXIdxEntity_CXXConversionFunction,
            CXIdxEntityKind.CXIdxEntity_CXXTypeAlias,
            CXIdxEntityKind.CXIdxEntity_CXXInterface -> println("SKIP C++ DECLARATION")
            else                                     -> TODO("NOT SUPPORTED: $kind")
        }
    }

    private fun reportError(cause: Throwable) {
        reportedErrors.addLast(cause)
    }

    private fun ensureNoErrors() {
        if (reportedErrors.isEmpty()) return

        val message = reportedErrors.joinToString("\n - ", "Indexing failed:\n - ", transform = Throwable::toString)
//        reportedErrors.forEach {
//            it.printStackTrace()
//        }
        error(message)
    }

    fun run(index: CXIndex, translationUnit: CXTranslationUnit) {
        memScoped {
            useStableRef(this@CxIndexRunner) { runnerRef ->
                val indexerCallbacks = alloc<IndexerCallbacks> {
                    diagnostic = staticCFunction { clientData, diagnosticSet, _ ->
                        unwrapRunnerRef(clientData) { it.diagnostic(diagnosticSet!!) }
                    }
                    enteredMainFile = staticCFunction { clientData, mainFile, _ ->
                        unwrapRunnerRef(clientData) { it.enteredMainFile(mainFile!!) }
                        null
                    }
                    ppIncludedFile = staticCFunction { clientData, file ->
                        unwrapRunnerRef(clientData) { it.ppIncludedFile(file!!.pointed) }
                        null
                    }
                    indexDeclaration = staticCFunction { clientData, info ->
                        unwrapRunnerRef(clientData) { it.indexDeclaration(info!!.pointed) }
                    }
                }

                useIndexAction(index) { action ->
                    val result = clang_indexTranslationUnit(
                        action,
                        runnerRef.asCPointer(),
                        indexerCallbacks.ptr,
                        sizeOf<IndexerCallbacks>().convert(),
                        0u,
                        translationUnit
                    )
                    if (result != 0) error("clang_indexTranslationUnit returned $result")
                }
            }
        }
        ensureNoErrors()
    }

    private companion object {
        private inline fun unwrapRunnerRef(clientData: CXClientData?, block: (runner: CxIndexRunner) -> Unit) {
            val runner = clientData!!.asStableRef<CxIndexRunner>().get()
            try {
                block(runner)
            } catch (cause: Throwable) {
                runner.reportError(cause)
            }
        }
    }
}

private inline fun <T> useIndexAction(index: CXIndex, block: (action: CXIndexAction) -> T): T {
    val action = checkNotNull(clang_IndexAction_create(index)) { "IndexAction is null" }
    try {
        return block(action)
    } finally {
        clang_IndexAction_dispose(action)
    }
}

private fun <T : Any, R> useStableRef(value: T, block: (ref: StableRef<T>) -> R): R {
    val ref = StableRef.create(value)
    try {
        return block(ref)
    } finally {
        ref.dispose()
    }
}
