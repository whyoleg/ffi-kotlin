package dev.whyoleg.foreign.clang.compiler

import dev.whyoleg.foreign.clang.compiler.libclang.*
import dev.whyoleg.foreign.clang.compiler.libclang.CXDiagnosticSeverity.*
import kotlinx.cinterop.*

internal val CXTranslationUnit.cursor: CValue<CXCursor> get() = clang_getTranslationUnitCursor(this)
internal val CXTranslationUnit.spelling: String get() = clang_getTranslationUnitSpelling(this).useString()!!

internal inline fun <R> useTranslationUnit(
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
            num_unsaved_files = 0u,
            options = CXTranslationUnit_SkipFunctionBodies or CXTranslationUnit_DetailedPreprocessingRecord,
            out_TU = resultVar.ptr
        )

        if (errorCode != CXErrorCode.CXError_Success) {
            // TODO: check diagnostics here
            error(
                """|parseTranslationUnit failed with $errorCode
                   |sourceFile = $file
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

private fun checkDiagnostics(
    translationUnit: CXTranslationUnit,
    printNonErrors: Boolean = true
) {
    // TODO: decide on options
    val options = clang_defaultDiagnosticDisplayOptions()
    var hasErrors = false
    repeat(clang_getNumDiagnostics(translationUnit).toInt()) { index ->
        val diagnostic = clang_getDiagnostic(translationUnit, index.toUInt())
        try {
            val severity = clang_getDiagnosticSeverity(diagnostic)
            val isError = severity == CXDiagnostic_Error || severity == CXDiagnostic_Fatal
            if (isError) hasErrors = true
            if (printNonErrors || isError) {
                val level = when (severity) {
                    CXDiagnostic_Warning -> "WARNING: "
                    CXDiagnostic_Note  -> "NOTE:    "
                    CXDiagnostic_Error -> "ERROR:   "
                    CXDiagnostic_Fatal -> "FATAL:   "
                    CXDiagnostic_Ignored -> "IGNORED: "
                    else               -> "UNKNOWN: "
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
