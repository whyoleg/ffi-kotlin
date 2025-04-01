package dev.whyoleg.foreign.tool.clang

import dev.whyoleg.foreign.tool.libclang.*
import dev.whyoleg.foreign.tool.libclang.CXDiagnosticSeverity.*
import kotlinx.cinterop.*

internal val CXTranslationUnit.cursor: CValue<CXCursor> get() = clang_getTranslationUnitCursor(this)
internal val CXTranslationUnit.spelling: String get() = clang_getTranslationUnitSpelling(this).useString()!!

internal inline fun <R> useTranslationUnit(
    index: CXIndex,
    headers: Set<String>,
    compilerArgs: List<String>,
    block: (CXTranslationUnit) -> R,
): R {
    require(headers.isNotEmpty()) { "Headers set can't be empty" }

    val tempFileName = "foreign_kotlin_temporary.h"
    val headersContent = buildString {
        headers.forEach { header ->
            appendLine("#include <$header>")
        }
    }
    val translationUnit = memScoped {
        val resultVar = alloc<CXTranslationUnitVar>()
        val errorCode = clang_parseTranslationUnit2(
            CIdx = index,
            source_filename = tempFileName,
            command_line_args = allocArray(compilerArgs.size) { value = compilerArgs[it].cstr.ptr },
            num_command_line_args = compilerArgs.size,
            unsaved_files = alloc<CXUnsavedFile> {
                @OptIn(UnsafeNumber::class)
                Length = headersContent.length.convert()
                Contents = headersContent.cstr.ptr
                Filename = tempFileName.cstr.ptr
            }.ptr,
            num_unsaved_files = 1u,
            options = CXTranslationUnit_SkipFunctionBodies or CXTranslationUnit_DetailedPreprocessingRecord,
            out_TU = resultVar.ptr
        )

        if (errorCode != CXErrorCode.CXError_Success) {
            // TODO: check diagnostics here
            error(
                """|parseTranslationUnit failed with $errorCode
                   |headers = $headers
                   |arguments = ${compilerArgs.joinToString(" ")}
                """.trimMargin()
            )
        }

        resultVar.value!!
    }
    try {
        checkDiagnostics(translationUnit)
        return block(translationUnit)
    } finally {
        clang_disposeTranslationUnit(translationUnit)
    }
}

private fun checkDiagnostics(translationUnit: CXTranslationUnit) {
    // TODO: decide on options
    val options = clang_defaultDiagnosticDisplayOptions()
    var hasErrors = false
    repeat(clang_getNumDiagnostics(translationUnit).toInt()) { index ->
        val diagnostic = clang_getDiagnostic(translationUnit, index.toUInt())
        try {
            val severity = clang_getDiagnosticSeverity(diagnostic)
            val isError = severity == CXDiagnostic_Error || severity == CXDiagnostic_Fatal
            if (isError) hasErrors = true
            if (isError) {
                val level = when (severity) {
                    CXDiagnostic_Warning -> "WARNING: "
                    CXDiagnostic_Note    -> "NOTE:    "
                    CXDiagnostic_Error   -> "ERROR:   "
                    CXDiagnostic_Fatal   -> "FATAL:   "
                    CXDiagnostic_Ignored -> "IGNORED: "
                }
                val message = clang_formatDiagnostic(diagnostic, options).useString()!!
                println("$level$message")
            }
        } finally {
            clang_disposeDiagnostic(diagnostic)
        }
    }
    check(!hasErrors) { "There were errors during parsing translation unit, please consult logged messages" }
}
