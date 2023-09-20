package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.internal.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import kotlinx.cinterop.*

internal class PrimitiveCollector : CxIndexer<Map<CxPrimitiveDataType, Int>> {
    private val reportedErrors = ArrayDeque<Throwable>()
    private val types = mutableMapOf(
        CxPrimitiveDataType.Void to 0,
        CxPrimitiveDataType.Bool to Byte.SIZE_BYTES
    )

    override fun enteredMainFile(mainFile: CXFile) {}
    override fun ppIncludedFile(fileInfo: CXIdxIncludedFileInfo) {}

    override fun indexDeclaration(declarationInfo: CXIdxDeclInfo) {
        val entityInfo = declarationInfo.entityInfo!!.pointed
        if (entityInfo.kind != CXIdxEntityKind.CXIdxEntity_Struct) return

        val cursor = declarationInfo.cursor.readValue()
        visitFields(cursor.type) { fieldCursor ->
            val typeKind = fieldCursor.type.kind
            val typeSize = clang_Type_getSizeOf(fieldCursor.type).toInt()
            val type = when (typeKind) {
                CXTypeKind.CXType_Char_U,
                CXTypeKind.CXType_Char_S     -> CxPrimitiveDataType.Char
                CXTypeKind.CXType_SChar      -> CxPrimitiveDataType.SignedChar
                CXTypeKind.CXType_UChar      -> CxPrimitiveDataType.UnsignedChar
                CXTypeKind.CXType_Short      -> CxPrimitiveDataType.Short
                CXTypeKind.CXType_UShort     -> CxPrimitiveDataType.UnsignedShort
                CXTypeKind.CXType_Int        -> CxPrimitiveDataType.Int
                CXTypeKind.CXType_UInt       -> CxPrimitiveDataType.UnsignedInt
                CXTypeKind.CXType_Long       -> CxPrimitiveDataType.Long
                CXTypeKind.CXType_ULong      -> CxPrimitiveDataType.UnsignedLong
                CXTypeKind.CXType_LongLong   -> CxPrimitiveDataType.LongLong
                CXTypeKind.CXType_ULongLong  -> CxPrimitiveDataType.UnsignedLongLong
                CXTypeKind.CXType_Int128     -> CxPrimitiveDataType.Int128
                CXTypeKind.CXType_UInt128    -> CxPrimitiveDataType.UnsignedInt128
                CXTypeKind.CXType_Float      -> CxPrimitiveDataType.Float
                CXTypeKind.CXType_Double     -> CxPrimitiveDataType.Double
                CXTypeKind.CXType_LongDouble -> CxPrimitiveDataType.LongDouble
                else                         -> error("wrong type: $typeKind")
            }
            check(types.put(type, typeSize) == null) { "$type already registered" }
            CXVisitorResult.CXVisit_Continue
        }
    }

    override fun diagnostic(diagnosticSet: CXDiagnosticSet) {
        repeat(clang_getNumDiagnosticsInSet(diagnosticSet).toInt()) { i ->
            val diagnostic = clang_getDiagnosticInSet(diagnosticSet, i.toUInt()) ?: return@repeat
            try {
                val severity = clang_getDiagnosticSeverity(diagnostic)
                val format = clang_formatDiagnostic(diagnostic, clang_defaultDiagnosticDisplayOptions()).useString()
                reportedErrors.addLast(IllegalStateException("$severity | $format"))
            } finally {
                clang_disposeDiagnostic(diagnostic)
            }
        }
    }

    override fun reportError(cause: Throwable) {
        reportedErrors.addLast(cause)
    }

    override fun buildResult(): Map<CxPrimitiveDataType, Int> {
        if (reportedErrors.isNotEmpty()) error(
            reportedErrors.joinToString(
                prefix = "Indexing failed:\n - ",
                separator = "\n - ",
                transform = Throwable::toString
            )
        )

        check(types.keys == CxPrimitiveDataType.entries.toSet()) {
            "${types.keys} != ${CxPrimitiveDataType.entries.toSet()}"
        }
        return types
    }
}
