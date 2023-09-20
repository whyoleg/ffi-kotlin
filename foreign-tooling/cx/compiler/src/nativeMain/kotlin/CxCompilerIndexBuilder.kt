package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.internal.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import kotlinx.cinterop.*

internal interface CxCompilerIndexBuilder {
    fun function(cursor: CValue<CXCursor>): CxCompilerDeclarationId
    fun typedef(cursor: CValue<CXCursor>): CxCompilerDeclarationId
    fun record(cursor: CValue<CXCursor>): CxCompilerDeclarationId
    fun enum(cursor: CValue<CXCursor>): CxCompilerDeclarationId
}

internal fun cxCompilerIndexBuilder(): CxIndexer<CxCompilerIndex> = CxCompilerIndexBuilderImpl()

private class CxCompilerIndexBuilderImpl : CxCompilerIndexBuilder, CxIndexer<CxCompilerIndex> {
    private val typedefs = mutableMapOf<CxCompilerDeclarationId, CxCompilerTypedef>()
    private val enums = mutableMapOf<CxCompilerDeclarationId, CxCompilerEnum>()
    private val records = mutableMapOf<CxCompilerDeclarationId, CxCompilerRecord>()
    private val functions = mutableMapOf<CxCompilerDeclarationId, CxCompilerFunction>()

    private val headerByPath = mutableMapOf<String, String>()

    private val dummyRecord = CxCompilerRecord(
        id = CxCompilerDeclarationId(""),
        declarationName = null,
        headerName = null,
        isUnion = false,
        members = null
    )

    private val reportedErrors = ArrayDeque<Throwable>()

    override fun function(cursor: CValue<CXCursor>): CxCompilerDeclarationId = save(functions, cursor, null, ::buildFunction)
    override fun typedef(cursor: CValue<CXCursor>): CxCompilerDeclarationId = save(typedefs, cursor, null, ::buildTypedef)
    override fun record(cursor: CValue<CXCursor>): CxCompilerDeclarationId = save(records, cursor, dummyRecord, ::buildRecord)
    override fun enum(cursor: CValue<CXCursor>): CxCompilerDeclarationId = save(enums, cursor, null, ::buildEnum)

    private fun <T : CxCompilerDeclaration> save(
        declarations: MutableMap<CxCompilerDeclarationId, T>,
        cursor: CValue<CXCursor>,
        dummy: T?,
        block: (
            id: CxCompilerDeclarationId,
            declarationName: String?,
            headerName: String?,
            cursor: CValue<CXCursor>
        ) -> T,
    ): CxCompilerDeclarationId {
        val id = clang_getCursorUSR(cursor).useString().let {
            checkNotNull(it) { "USR = null" }
            check(it.isNotBlank()) { "USR is blank" }
            CxCompilerDeclarationId(it)
        }

        if (id in declarations) return id

        val name = when (clang_Cursor_isAnonymous(cursor)) {
            1U   -> null
            else -> cursor.spelling
        }
        val headerName = headerByPath[clang_getFileName(cursor.locationFile).useString()]
        declarations[id] = when (dummy) {
            null -> block(id, name, headerName, cursor)
            else -> {
                //save dummy to be able to reference it in recursive records
                declarations[id] = dummy
                try {
                    block(id, name, headerName, cursor)
                } catch (cause: Throwable) {
                    //drop dummy if failed
                    declarations.remove(id)
                    throw cause
                }
            }
        }
        return id
    }

    override fun enteredMainFile(mainFile: CXFile) {
        val fileName = clang_getFileName(mainFile).useString()!!
        // TODO header name?
        include("_/main.h", fileName)
    }

    override fun ppIncludedFile(fileInfo: CXIdxIncludedFileInfo) {
        val includeName = fileInfo.filename?.toKString()
        val fileName = clang_getFileName(fileInfo.file).useString()

        if (includeName != null && fileName != null) {
            include(includeName, fileName)
        } else {
            reportError(IllegalStateException("ppIncludedFile | $includeName | $fileName"))
        }
    }

    private fun include(name: String, path: String) {
        headerByPath[path] = name
    }

    override fun indexDeclaration(declarationInfo: CXIdxDeclInfo) {
        val entityInfo = declarationInfo.entityInfo!!.pointed
        val cursor = declarationInfo.cursor.readValue()
        when (val kind = entityInfo.kind) {
            CXIdxEntityKind.CXIdxEntity_Function     -> function(cursor)
            CXIdxEntityKind.CXIdxEntity_Struct,
            CXIdxEntityKind.CXIdxEntity_Union        -> record(cursor)
            CXIdxEntityKind.CXIdxEntity_Typedef      -> typedef(cursor)
            CXIdxEntityKind.CXIdxEntity_Enum         -> enum(cursor)
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

    override fun diagnostic(diagnosticSet: CXDiagnosticSet) {
        repeat(clang_getNumDiagnosticsInSet(diagnosticSet).toInt()) { i ->
            val diagnostic = clang_getDiagnosticInSet(diagnosticSet, i.toUInt()) ?: return@repeat
            try {
                val severity = clang_getDiagnosticSeverity(diagnostic)
                val format = clang_formatDiagnostic(diagnostic, clang_defaultDiagnosticDisplayOptions()).useString()
//                    val location = clang_getDiagnosticLocation(diagnostic)
                //println("D: $severity | $format")
                // TODO: add to reportedErrors
            } finally {
                clang_disposeDiagnostic(diagnostic)
            }
        }
    }

    override fun reportError(cause: Throwable) {
        reportedErrors.addLast(cause)
    }

    override fun buildResult(): CxCompilerIndex {
        if (reportedErrors.isNotEmpty()) {

            val message = reportedErrors.joinToString("\n - ", "Indexing failed:\n - ", transform = Throwable::toString)
//        reportedErrors.forEach {
//            it.printStackTrace()
//        }
            error(message)
        }

        return CxCompilerIndex(
            typedefs = typedefs.toMap(),
            records = records.toMap(),
            enums = enums.toMap(),
            functions = functions.toMap(),
        )
    }
}
