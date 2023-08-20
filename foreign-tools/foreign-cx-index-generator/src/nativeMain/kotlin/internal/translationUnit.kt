package dev.whyoleg.foreign.index.cx.generator.internal

import dev.whyoleg.foreign.index.cx.clang.*
import kotlinx.cinterop.*

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
