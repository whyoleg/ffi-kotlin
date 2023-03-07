package dev.whyoleg.ffi.c.index

import dev.whyoleg.ffi.c.index.clang.*
import kotlinx.cinterop.*
import okio.*
import okio.ByteString.Companion.encodeUtf8

public fun buildCIndex(
    headers: List<String>,
    includeDirectories: List<String>,
): CIndex {
    val headersFile = createTemporaryFile(headers)
    val compilerArgs = includeDirectories.map { "-I$it" }

    val cIndex = CIndexImpl()

    val indexer = ClangIndexer(cIndex)
    withIndex { index ->
        withTranslationUnit(index, headersFile, compilerArgs) { translationUnit ->
            indexTranslationUnit(index, translationUnit, indexer)
        }
    }
    indexer.ensureNoErrors()

    return cIndex
}

private class ClangIndexer(private val registry: CIndexRegistry) {
    private val reportedErrors = ArrayDeque<Throwable>()

    fun diagnostic(diagnosticSet: CXDiagnosticSet) {
        repeat(clang_getNumDiagnosticsInSet(diagnosticSet).toInt()) { i ->
            val diagnostic = clang_getDiagnosticInSet(diagnosticSet, i.toUInt()) ?: return@repeat
            try {
                val severity = clang_getDiagnosticSeverity(diagnostic)
                val format = clang_formatDiagnostic(diagnostic, clang_defaultDiagnosticDisplayOptions()).useString()
//                    val location = clang_getDiagnosticLocation(diagnostic)
                println("D: $severity | $format")
            } finally {
                clang_disposeDiagnostic(diagnostic)
            }
        }
    }

    fun enteredMainFile(mainFile: CXFile) {
        println("CALL: enteredMainFile | ${clang_getFileName(mainFile).useString()}")
    }

    fun ppIncludedFile(fileInfo: CXIdxIncludedFileInfo) {
        val includeName = fileInfo.filename?.toKString()
        val fileName = clang_getFileName(fileInfo.file).useString()

        if (includeName != null && fileName != null) {
            registry.include(includeName, fileName)
        } else {
            println("SKIP: ppIncludedFile | $includeName | $fileName")
        }
    }

    fun indexDeclaration(declarationInfo: CXIdxDeclInfo) {
        val entityInfo = declarationInfo.entityInfo!!.pointed
        val cursor = declarationInfo.cursor.readValue()
        when (entityInfo.kind) {
            CXIdxEntityKind.CXIdxEntity_Function  -> registry.function(cursor)
            CXIdxEntityKind.CXIdxEntity_Struct,
            CXIdxEntityKind.CXIdxEntity_Union,
                                                  -> registry.struct(cursor)
            CXIdxEntityKind.CXIdxEntity_Typedef   -> registry.typedef(cursor)
            CXIdxEntityKind.CXIdxEntity_Enum      -> registry.enum(cursor)
            CXIdxEntityKind.CXIdxEntity_Field,
            CXIdxEntityKind.CXIdxEntity_EnumConstant,
                                                  -> {
            } //TODO: ignore?
            CXIdxEntityKind.CXIdxEntity_Unexposed -> {}//TODO("CXIdxEntity_Unexposed")
            CXIdxEntityKind.CXIdxEntity_Variable  -> {}//TODO("CXIdxEntity_Variable")

            CXIdxEntityKind.CXIdxEntity_ObjCClass,
            CXIdxEntityKind.CXIdxEntity_ObjCProtocol,
            CXIdxEntityKind.CXIdxEntity_ObjCCategory,
            CXIdxEntityKind.CXIdxEntity_ObjCInstanceMethod,
            CXIdxEntityKind.CXIdxEntity_ObjCClassMethod,
            CXIdxEntityKind.CXIdxEntity_ObjCProperty,
            CXIdxEntityKind.CXIdxEntity_ObjCIvar,
                                                  -> println("SKIP OBJ-C DECLARATION")
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
            CXIdxEntityKind.CXIdxEntity_CXXInterface,
            CXIdxEntityKind.CXIdxEntity_CXXConcept,
                                                  -> println("SKIP C++ DECLARATION")
        }
    }

    fun reportError(cause: Throwable) {
        reportedErrors.addLast(cause)
    }

    fun ensureNoErrors() {
        if (reportedErrors.isEmpty()) return

        val message = reportedErrors.joinToString("\n - ", "Indexing failed:\n - ", transform = Throwable::toString)
//        reportedErrors.forEach {
//            it.printStackTrace()
//        }
        error(message)
    }
}

private fun createTemporaryFile(headers: List<String>): String {
    val fileInfo = headers.joinToString("\n") { "#include <$it>" }
    val hashInfo = fileInfo.encodeUtf8().sha1().hex()
    val headersPath = FileSystem.SYSTEM_TEMPORARY_DIRECTORY.resolve("$hashInfo.h")
    val headersExists = FileSystem.SYSTEM.exists(headersPath)
    println("$hashInfo -> $headersExists")
    if (!headersExists) FileSystem.SYSTEM.write(headersPath) { writeUtf8(fileInfo) }
    return headersPath.toString()
}

private inline fun <R> withIndex(
    excludeDeclarationsFromPCH: Boolean = false, // disables visitChildren to visit declarations from imported translation units
    displayDiagnostics: Boolean = true,
    block: (index: CXIndex) -> R,
): R {
    val index = checkNotNull(
        clang_createIndex(
            excludeDeclarationsFromPCH = if (excludeDeclarationsFromPCH) 1 else 0,
            displayDiagnostics = if (displayDiagnostics) 1 else 0
        )
    )
    try {
        return block(index)
    } finally {
        clang_disposeIndex(index)
    }
}

private inline fun <R> withTranslationUnit(
    index: CXIndex,
    file: String,
    compilerArgs: List<String>,
    block: (CXTranslationUnit) -> R,
): R {
    val translationUnit = memScoped {
        val resultVar = alloc<CXTranslationUnitVar>()
        val errorCode = clang_parseTranslationUnit2(
            CIdx = index,
            source_filename = file,
            command_line_args = allocArray(compilerArgs.size) { value = compilerArgs[it].cstr.ptr },
            num_command_line_args = compilerArgs.size,
            unsaved_files = null,
            num_unsaved_files = 0,
            options = CXTranslationUnit_SkipFunctionBodies or CXTranslationUnit_DetailedPreprocessingRecord,
            out_TU = resultVar.ptr
        )

        if (errorCode != CXErrorCode.CXError_Success) {
            error(
                """|clang_parseTranslationUnit2 failed with $errorCode
               |sourceFile = $file
               |arguments = ${compilerArgs.joinToString(" ")}
            """.trimMargin()
            )
        }

        resultVar.value!!
    }
    try {
        return block(translationUnit)
    } finally {
        clang_disposeTranslationUnit(translationUnit)
    }
}

private fun indexTranslationUnit(index: CXIndex, translationUnit: CXTranslationUnit, indexer: ClangIndexer): Unit = memScoped {
    val indexerRef = StableRef.create(indexer)
    val indexerCallbacks = alloc<IndexerCallbacks> {
        diagnostic = staticCFunction { clientData, diagnosticSet, _ ->
            useIndexer(clientData) { it.diagnostic(diagnosticSet!!) }
        }
        enteredMainFile = staticCFunction { clientData, mainFile, _ ->
            useIndexer(clientData) { it.enteredMainFile(mainFile!!) }
            null
        }
        ppIncludedFile = staticCFunction { clientData, file ->
            useIndexer(clientData) { it.ppIncludedFile(file!!.pointed) }
            null
        }
        indexDeclaration = staticCFunction { clientData, info ->
            useIndexer(clientData) { it.indexDeclaration(info!!.pointed) }
        }
    }

    val indexAction = clang_IndexAction_create(index)
    try {
        val result = clang_indexTranslationUnit(
            indexAction,
            indexerRef.asCPointer(),
            indexerCallbacks.ptr,
            sizeOf<IndexerCallbacks>().convert(),
            0,
            translationUnit
        )
        if (result != 0) {
            throw Error("clang_indexTranslationUnit returned $result")
        }
    } finally {
        clang_IndexAction_dispose(indexAction)
    }
}

private inline fun useIndexer(clientData: CXClientData?, block: (ClangIndexer) -> Unit) {
    val indexer = clientData!!.asStableRef<ClangIndexer>().get()
    try {
        block(indexer)
    } catch (cause: Throwable) {
        indexer.reportError(cause)
    }
}
