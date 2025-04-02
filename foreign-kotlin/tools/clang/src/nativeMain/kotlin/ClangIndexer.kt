package dev.whyoleg.foreign.tool.clang

import dev.whyoleg.foreign.tool.clang.api.*
import dev.whyoleg.foreign.tool.libclang.*
import dev.whyoleg.foreign.tool.libclang.CXCursorKind.*
import dev.whyoleg.foreign.tool.libclang.CXTypeKind.*
import kotlinx.cinterop.*

internal class ClangIndexer {
    // Path->Include
    private val files = mutableMapOf<String, CxDeclarationId>()

    // we need `visited` to handle recursive structs (where struct has a pointer to the same struct)
    private val visited = mutableSetOf<CxDeclarationId>()
    private val declarations = mutableMapOf<CxDeclarationId, CxDeclaration>()

    fun indexTranslationUnit(translationUnit: CXTranslationUnit) {
        // index includes first
        translationUnit.cursor.visitChildren { cursor ->
            if (cursor.kind == CXCursor_InclusionDirective) {
                val path = cursor.includedFile.fileName
                if (!files.containsKey(path)) {
                    files[path] = cursor.spelling ?: error("Include header is blank: ${cursor.debugString}")
                }
            }
        }

        // index declarations
        translationUnit.cursor.visitChildren { cursor ->
            when (cursor.kind) {
                CXCursor_InclusionDirective             -> {} // processed earlier
                CXCursor_VarDecl                        -> visitVariable(cursor)
                CXCursor_EnumDecl                       -> visitEnum(cursor)
                CXCursor_TypedefDecl                    -> visitTypedef(cursor)
                CXCursor_UnionDecl, CXCursor_StructDecl -> visitRecord(cursor)
                CXCursor_FunctionDecl                   -> visitFunction(cursor)

                // it looks like we just should skip it
                CXCursor_UnexposedDecl                  -> {}
                // skip for now
                CXCursor_MacroDefinition,
                CXCursor_MacroExpansion,
                                                        -> {
                }

                else                                    -> {
                    println("UNSUPPORTED CURSOR: ${cursor.debugString}")
                }
            }
        }

        fun printBuiltins(declarations: Map<CxDeclarationId, CxDeclaration>) {
            declarations.forEach {
                if (it.value.header.isNullOrEmpty()) println("  ${it.value}")
            }
        }

        println("BUILTINS")
        printBuiltins(declarations)
        println()
    }

    fun buildIndex(): CxIndex = CxIndex(declarations.values.toList())

    private fun visit(cursor: CValue<CXCursor>, block: (cursor: CValue<CXCursor>) -> CxDeclarationData) {
        val id = cursor.usr
        if (!visited.add(id)) return
        val canonicalCursor = cursor.canonical
        val data = block(canonicalCursor)

        val declaration = CxDeclaration(
            id = canonicalCursor.usr,
            name = if (canonicalCursor.isAnonymous) null else canonicalCursor.spelling,
            header = canonicalCursor.location.file?.fileName?.let(files::getValue),
            data = data
        )
        declarations.put(id, declaration)
    }

    // `visit` provides canonical cursor
    private fun visitVariable(cursor: CValue<CXCursor>) = visit(cursor, ::parseVariable)
    private fun visitEnum(cursor: CValue<CXCursor>) = visit(cursor, ::parseEnum)
    private fun visitTypedef(cursor: CValue<CXCursor>) = visit(cursor, ::parseTypedef)
    private fun visitRecord(cursor: CValue<CXCursor>) = visit(cursor, ::parseRecord)
    private fun visitFunction(cursor: CValue<CXCursor>) = visit(cursor, ::parseFunction)

    private fun parseVariable(cursor: CValue<CXCursor>): CxVariableData {
        cursor.ensureKind(CXCursor_VarDecl)
        checkNotNull(cursor.spelling) { "Variable should have a name: ${cursor.debugString}" }

        val tag = cursor.debugString

        return CxVariableData(
            isConst = clang_isConstQualifiedType(cursor.type) > 0u, // TODO: recheck
            type = parseType(tag, cursor.type),
        )
    }

    private fun parseEnum(cursor: CValue<CXCursor>): CxEnumData {
        cursor.ensureKind(CXCursor_EnumDecl)

        val tag = cursor.debugString

        return CxEnumData(
            constants = buildList {
                cursor.visitChildren { constantCursor ->
                    constantCursor.ensureKind(CXCursor_EnumConstantDecl)
                    add(
                        CxEnumConstant(
                            name = constantCursor.spelling
                                ?: error("Enum constant should have a name: ${constantCursor.debugString} in $tag"),
                            value = clang_getEnumConstantDeclValue(constantCursor)
                        )
                    )
                }
            }
        )
    }

    private fun parseTypedef(cursor: CValue<CXCursor>): CxTypedefData {
        cursor.ensureKind(CXCursor_TypedefDecl)
        checkNotNull(cursor.spelling) { "Typedef should have a name: ${cursor.debugString}" }

        val tag = cursor.debugString

        return CxTypedefData(
            aliasedType = parseType(tag, clang_getTypedefDeclUnderlyingType(cursor)),
            resolvedType = parseType(tag, clang_getCanonicalType(cursor.type))
        )
    }

    private fun parseRecord(cursor: CValue<CXCursor>): CxDeclarationData {
        cursor.ensureKind(CXCursor_StructDecl, CXCursor_UnionDecl)

        val definitionCursor = cursor.definition

        return when (definitionCursor.kind) {
            CXCursor_InvalidFile -> CxOpaqueData
            else                 -> parseRecordDefinition(definitionCursor)
        }
    }

    private fun parseRecordDefinition(cursor: CValue<CXCursor>): CxRecordData {
        val tag = cursor.debugString

        val fields = mutableListOf<CxRecordField>()
        val anonymousRecords = mutableSetOf<CxDeclarationId>()

        cursor.visitChildren { fieldCursor ->
            when (fieldCursor.kind) {
                CXCursor_FieldDecl                      -> fields.add(
                    CxRecordField(
                        name = fieldCursor.spelling,
                        type = parseType(tag, fieldCursor.type),
                        // TODO: bit offset for anonymous records
                        bitOffset = clang_Cursor_getOffsetOfField(fieldCursor).also {
                            check(it >= 0) { "Unknown field offset $it field ${fieldCursor.debugString} in $tag" }
                        },
                        bitWidth = clang_getFieldDeclBitWidth(fieldCursor).takeIf { it > 0 }
                    )
                )

                CXCursor_StructDecl, CXCursor_UnionDecl -> {
                    visitRecord(fieldCursor)
                    if (fieldCursor.isAnonymous) {
                        anonymousRecords.add(fieldCursor.usr)
                    }
                }
                // TODO: how to handle those?
                CXCursor_UnexposedAttr,
                CXCursor_PackedAttr,
                CXCursor_AlignedAttr
                                                        -> {
                }

                else                                    -> fieldCursor.throwWrongKind()
            }
        }

        return CxRecordData(
            isUnion = when (cursor.kind) {
                CXCursor_StructDecl -> false
                CXCursor_UnionDecl  -> true
                else                -> cursor.throwWrongKind()
            },
            byteSize = clang_Type_getSizeOf(cursor.type).also { check(it > 0) { "wrong sizeOf(=$it) result in $tag" } },
            byteAlignment = clang_Type_getAlignOf(cursor.type).also { check(it > 0) { "wrong alignOf(=$it) result in $tag" } },
            fields = fields,
            anonymousRecords = anonymousRecords
        )
    }

    private fun parseFunction(cursor: CValue<CXCursor>): CxFunctionData {
        cursor.ensureKind(CXCursor_FunctionDecl)
        checkNotNull(cursor.spelling) { "Function should have a name: ${cursor.debugString}" }

        val tag = cursor.debugString

        return CxFunctionData(
            isVariadic = clang_isFunctionTypeVariadic(cursor.type) > 0U,
            returnType = parseType(tag, clang_getCursorResultType(cursor)),
            parameters = buildList {
                cursor.forEachArgument { argCursor ->
                    argCursor.ensureKind(CXCursor_ParmDecl)

                    add(
                        CxFunctionParameter(
                            name = argCursor.spelling,
                            type = parseType(tag, argCursor.type)
                        )
                    )
                }
            }
        )
    }

    private fun parseType(tag: String, type: CValue<CXType>): CxType = when (type.kind) {
        CXType_Void                                  -> CxType.Void
        CXType_Bool                                  -> CxType.Bool

        CXType_Char_U, CXType_Char_S                 -> CxType.Number(CxNumber.Char)
        CXType_SChar                                 -> CxType.Number(CxNumber.SignedChar)
        CXType_UChar                                 -> CxType.Number(CxNumber.UnsignedChar)
        CXType_Short                                 -> CxType.Number(CxNumber.Short)
        CXType_UShort                                -> CxType.Number(CxNumber.UnsignedShort)
        CXType_Int                                   -> CxType.Number(CxNumber.Int)
        CXType_UInt                                  -> CxType.Number(CxNumber.UnsignedInt)
        CXType_Long                                  -> CxType.Number(CxNumber.Long)
        CXType_ULong                                 -> CxType.Number(CxNumber.UnsignedLong)
        CXType_LongLong                              -> CxType.Number(CxNumber.LongLong)
        CXType_ULongLong                             -> CxType.Number(CxNumber.UnsignedLongLong)
        CXType_Int128                                -> CxType.Number(CxNumber.Int128)
        CXType_UInt128                               -> CxType.Number(CxNumber.UnsignedInt128)
        CXType_Float                                 -> CxType.Number(CxNumber.Float)
        CXType_Float16                               -> CxType.Number(CxNumber.Float16)
        CXType_Double                                -> CxType.Number(CxNumber.Double)
        CXType_LongDouble                            -> CxType.Number(CxNumber.LongDouble)
        CXType_Complex                               -> CxType.Complex(
            // TODO: really strange thing... (based on chatgpt)
            when (type.spelling) {
                "_Complex float", "float _Complex"             -> CxNumber.Float
                "_Complex _Float16", "_Float16 _Complex"       -> CxNumber.Float16
                "_Complex double", "double _Complex"           -> CxNumber.Double
                "_Complex long double", "long double _Complex" -> CxNumber.LongDouble
                else                                           -> error("WRONG complex type ${type.spelling}")
            }
        )

        // artificial type
        CXType_Elaborated                            -> parseType(
            tag,
            clang_Type_getNamedType(type)
        )

        // composite types
        CXType_Pointer                               -> CxType.Pointer(
            parseType(tag, clang_getPointeeType(type))
        )

        CXType_BlockPointer                          -> CxType.BlockPointer(
            parseType(tag, clang_getPointeeType(type)) as CxType.Function
        )

        CXType_Enum                                  -> {
            visitEnum(type.cursor)
            CxType.Enum(type.cursor.usr)
        }

        CXType_Record                                -> {
            visitRecord(type.cursor)
            CxType.Record(type.cursor.usr)
        }

        CXType_Typedef                               -> {
            visitTypedef(type.cursor)
            CxType.Typedef(type.cursor.usr)
        }

        CXType_FunctionProto, CXType_FunctionNoProto -> CxType.Function(
            returnType = parseType(tag, clang_getResultType(type)),
            parameters = buildList {
                repeat(clang_getNumArgTypes(type)) {
                    add(parseType(tag, clang_getArgType(type, it.convert())))
                }
            }
        )

        CXType_IncompleteArray                       -> CxType.Array(
            parseType(tag, clang_getArrayElementType(type)),
            null
        )

        CXType_ConstantArray                         -> CxType.Array(
            parseType(tag, clang_getArrayElementType(type)),
            clang_getArraySize(type).also {
                check(it in 0..Int.MAX_VALUE) { "Array size max value is ${Int.MAX_VALUE}, but was $it in $tag" }
            }.toInt()
        )

        // TODO: recheck it later
        CXType_Vector                                -> CxType.Vector(
            parseType(tag, clang_getElementType(type)),
            clang_getNumElements(type).also {
                check(it in 0..Int.MAX_VALUE) { "Array size max value is ${Int.MAX_VALUE}, but was $it in $tag" }
            }.toInt()
        )

        // unknown unsupported
        else                                         -> {
            println("UNKNOWN UNSUPPORTED TYPE: ${type.debugString} in $tag")
            CxType.Unsupported(type.debugString)
        }
    }
}
