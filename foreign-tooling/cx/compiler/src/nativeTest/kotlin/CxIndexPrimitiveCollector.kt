package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.internal.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import kotlinx.cinterop.*

internal class CxIndexPrimitiveCollector : CxIndexer<Map<CxBuiltinType, Int>> {
    private val reportedErrors = ArrayDeque<Throwable>()
    private val types = mutableMapOf<CxBuiltinType, Int>()

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
                CXTypeKind.CXType_Char_S     -> CxBuiltinType.Char
                CXTypeKind.CXType_SChar      -> CxBuiltinType.SignedChar
                CXTypeKind.CXType_UChar      -> CxBuiltinType.UnsignedChar
                CXTypeKind.CXType_Short      -> CxBuiltinType.Short
                CXTypeKind.CXType_UShort     -> CxBuiltinType.UnsignedShort
                CXTypeKind.CXType_Int        -> CxBuiltinType.Int
                CXTypeKind.CXType_UInt       -> CxBuiltinType.UnsignedInt
                CXTypeKind.CXType_Long       -> CxBuiltinType.Long
                CXTypeKind.CXType_ULong      -> CxBuiltinType.UnsignedLong
                CXTypeKind.CXType_LongLong   -> CxBuiltinType.LongLong
                CXTypeKind.CXType_ULongLong  -> CxBuiltinType.UnsignedLongLong
                CXTypeKind.CXType_Int128     -> CxBuiltinType.Int128
                CXTypeKind.CXType_UInt128    -> CxBuiltinType.UnsignedInt128
                CXTypeKind.CXType_Float      -> CxBuiltinType.Float
                CXTypeKind.CXType_Double     -> CxBuiltinType.Double
                CXTypeKind.CXType_LongDouble -> CxBuiltinType.LongDouble
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

    override fun buildResult(): Map<CxBuiltinType, Int> {
        if (reportedErrors.isNotEmpty()) error(
            reportedErrors.joinToString(
                prefix = "Indexing failed:\n - ",
                separator = "\n - ",
                transform = Throwable::toString
            )
        )

        check(types.keys == CxBuiltinType.entries.toSet()) {
            "${types.keys} != ${CxBuiltinType.entries.toSet()}"
        }
        return types
    }
}
