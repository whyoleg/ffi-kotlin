package dev.whyoleg.foreign.cx.index.generator

import dev.whyoleg.foreign.cx.index.clang.*
import dev.whyoleg.foreign.cx.index.generator.internal.*
import kotlinx.cinterop.*
import kotlin.test.*

internal class CxIndexPrimitiveCollector : CxIndexHandler() {
    override fun diagnostic(diagnosticSet: CXDiagnosticSet) {
        repeat(clang_getNumDiagnosticsInSet(diagnosticSet).toInt()) { i ->
            val diagnostic = clang_getDiagnosticInSet(diagnosticSet, i.toUInt()) ?: return@repeat
            try {
                val severity = clang_getDiagnosticSeverity(diagnostic)
                val format = clang_formatDiagnostic(diagnostic, clang_defaultDiagnosticDisplayOptions()).useString()
                println("D: $severity | $format")
            } finally {
                clang_disposeDiagnostic(diagnostic)
            }
        }
    }

    override fun enteredMainFile(mainFile: CXFile) {
        println("Header: ${clang_getFileName(mainFile).useString()}")
    }

    override fun ppIncludedFile(fileInfo: CXIdxIncludedFileInfo) {
        val includeName = fileInfo.filename?.toKString()
        val fileName = clang_getFileName(fileInfo.file).useString()
        println("Include: $includeName / $fileName")
    }

    override fun indexDeclaration(declarationInfo: CXIdxDeclInfo) {
        val entityInfo = declarationInfo.entityInfo!!.pointed
        val cursor = declarationInfo.cursor.readValue()

        if (entityInfo.kind != CXIdxEntityKind.CXIdxEntity_Struct) return

        buildMap {
            visitFields(cursor.type) { fieldCursor ->
                val name = fieldCursor.spelling
                val typeName = fieldCursor.type.spelling
                val typeKind = fieldCursor.type.kind
                val typeSize = clang_Type_getSizeOf(fieldCursor.type)

                println("$name: $typeName -> $typeKind = $typeSize")
                check(put(typeKind, typeSize) == null)
                CXVisitorResult.CXVisit_Continue
            }
        }.forEach {
            println(it)
        }
    }

    override fun unhandledError(cause: Throwable) {
        println("ERROR: $cause")
    }
}

class PrimitivesTest {

    @Test
    fun test() {
        val collector = CxIndexPrimitiveCollector()
        useIndex { index ->
            //TODO: arguments
            //TODO: file from resources
            useTranslationUnit(
                index,
                "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/foreign-tools/foreign-cx-index-generator/src/nativeTest/resources/primitives.h",
                emptyList()
            ) { translationUnit ->
                useIndexHandler(collector) { handlerPointer, callbacksPointer ->
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
            }
        }
    }
}
