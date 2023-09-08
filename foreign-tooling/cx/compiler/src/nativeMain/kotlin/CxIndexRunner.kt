package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.internal.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*
import kotlinx.cinterop.*

internal class CxIndexRunner(
    private val builder: CxIndexBuilder
) : CxIndexHandler() {
    private val reportedErrors = ArrayDeque<Throwable>()

    override fun diagnostic(diagnosticSet: CXDiagnosticSet) {
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

    override fun enteredMainFile(mainFile: CXFile) {
        val fileName = clang_getFileName(mainFile).useString()!!
        // TODO header name?
        builder.include("_/main.h", fileName)
    }

    override fun ppIncludedFile(fileInfo: CXIdxIncludedFileInfo) {
        val includeName = fileInfo.filename?.toKString()
        val fileName = clang_getFileName(fileInfo.file).useString()

        if (includeName != null && fileName != null) {
            builder.include(includeName, fileName)
        } else {
            unhandledError(IllegalStateException("ppIncludedFile | $includeName | $fileName"))
        }
    }

    override fun indexDeclaration(declarationInfo: CXIdxDeclInfo) {
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

    override fun unhandledError(cause: Throwable) {
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

    //TODO: extract
    fun run(index: CXIndex, translationUnit: CXTranslationUnit) {
        useIndexHandler(this) { handlerPointer, callbacksPointer ->
            useIndexAction(index) { action ->
                val result = clang_indexTranslationUnit(
                    action,
                    handlerPointer,
                    callbacksPointer,
                    sizeOf<IndexerCallbacks>().convert(),
                    0u,
                    translationUnit
                )
                if (result != 0) error("clang_indexTranslationUnit returned $result")
            }
        }
        ensureNoErrors()
    }
}
