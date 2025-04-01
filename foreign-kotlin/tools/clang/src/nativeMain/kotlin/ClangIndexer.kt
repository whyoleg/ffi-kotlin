package dev.whyoleg.foreign.tool.clang

import dev.whyoleg.foreign.tool.libclang.*
import dev.whyoleg.foreign.tool.libclang.CXCursorKind.*
import dev.whyoleg.foreign.tool.libclang.CXTypeKind.*
import dev.whyoleg.foreign.tool.clang.api.*
import kotlinx.cinterop.*

internal class ClangIndexer {
    // Path->Include
    private val files = mutableMapOf<String, CxDeclarationId>()

    private val visited = mutableSetOf<CxDeclarationId>()

    private val variables = mutableMapOf<CxDeclarationId, CxVariable>()
    private val enums = mutableMapOf<CxDeclarationId, CxEnum>()
    private val typedefs = mutableMapOf<CxDeclarationId, CxTypedef>()
    private val records = mutableMapOf<CxDeclarationId, CxRecord>()
    private val functions = mutableMapOf<CxDeclarationId, CxFunction>()

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

        fun printBuiltins(type: String, declarations: Map<CxDeclarationId, CxDeclaration>) {
            declarations.forEach {
                if (it.value.description.header.isNullOrEmpty()) println("  $type $it")
            }
        }

        println("BUILTINS")
        printBuiltins("variable :", variables)
        printBuiltins("enum     :", enums)
        printBuiltins("typedef  :", typedefs)
        printBuiltins("record   :", records)
        printBuiltins("function :", functions)
        println()
    }

    fun buildIndex(): CxIndex {
        return CxIndex(
            variables = variables.values.toList(),
            enums = enums.values.toList(),
            typedefs = typedefs.values.toList(),
            records = records.values.toList(),
            functions = functions.values.toList(),
        )
    }

    private inline fun <D : CxDeclaration> MutableMap<CxDeclarationId, D>.saveDeclaration(
        cursor: CValue<CXCursor>,
        block: (
            description: CxDeclarationDescription,
            cursor: CValue<CXCursor>
        ) -> D
    ) {
        val id = cursor.usr
        check(!containsKey(id)) { "$id is already saved" }

        val canonicalCursor = cursor.canonical
        put(id, block(description(canonicalCursor), canonicalCursor))
    }

    private fun description(cursor: CValue<CXCursor>): CxDeclarationDescription = CxDeclarationDescription(
        id = cursor.usr,
        name = if (cursor.isAnonymous) null else cursor.spelling,
        isAnonymous = cursor.isAnonymous,
        header = cursor.location.file?.fileName?.let(files::getValue)
    )

    private inline fun visit(cursor: CValue<CXCursor>, block: () -> Unit) {
        if (visited.add(cursor.usr)) block()
    }

    private fun visitVariable(cursor: CValue<CXCursor>) {
        visit(cursor) {
            checkNotNull(cursor.spelling) { "Variable should have a name: ${cursor.debugString}" }
            variables.saveDeclaration(cursor, ::parseVariable)
        }
    }

    private fun visitEnum(cursor: CValue<CXCursor>) {
        visit(cursor) {
            enums.saveDeclaration(cursor, ::parseEnum)
        }
    }

    private fun visitTypedef(cursor: CValue<CXCursor>) {
        visit(cursor) {
            checkNotNull(cursor.spelling) { "Typedef should have a name: ${cursor.debugString}" }
            typedefs.saveDeclaration(cursor, ::parseTypedef)
        }
    }

    private fun visitRecord(cursor: CValue<CXCursor>) {
        visit(cursor) {
            records.saveDeclaration(cursor, ::parseRecord)
        }
    }

    private fun visitFunction(cursor: CValue<CXCursor>) {
        visit(cursor) {
            checkNotNull(cursor.spelling) { "Function should have a name: ${cursor.debugString}" }
            functions.saveDeclaration(cursor, ::parseFunction)
        }
    }

    private fun parseVariable(description: CxDeclarationDescription, cursor: CValue<CXCursor>): CxVariable {
        cursor.ensureKind(CXCursor_VarDecl)

        val tag = cursor.debugString

        return CxVariable(
            description = description,
            isConst = clang_isConstQualifiedType(cursor.type) > 0u, // TODO: recheck
            type = parseType(tag, cursor.type),
        )
    }

    private fun parseEnum(description: CxDeclarationDescription, cursor: CValue<CXCursor>): CxEnum {
        cursor.ensureKind(CXCursor_EnumDecl)

        val tag = cursor.debugString

        return CxEnum(
            description = description,
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

    private fun parseTypedef(description: CxDeclarationDescription, cursor: CValue<CXCursor>): CxTypedef {
        cursor.ensureKind(CXCursor_TypedefDecl)

        val tag = cursor.debugString

        return CxTypedef(
            description = description,
            aliasedType = parseType(tag, clang_getTypedefDeclUnderlyingType(cursor)),
            resolvedType = parseType(tag, clang_getCanonicalType(cursor.type))
        )
    }

    private fun parseRecord(description: CxDeclarationDescription, cursor: CValue<CXCursor>): CxRecord {
        cursor.ensureKind(CXCursor_StructDecl, CXCursor_UnionDecl)

        val definitionCursor = cursor.definition

        return CxRecord(
            description = description,
            definition = when (definitionCursor.kind) {
                CXCursor_InvalidFile -> null
                else                 -> parseRecordDefinition(definitionCursor)
            }
        )
    }

    private fun parseRecordDefinition(cursor: CValue<CXCursor>): CxRecordDefinition {
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

        return CxRecordDefinition(
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

    private fun parseFunction(description: CxDeclarationDescription, cursor: CValue<CXCursor>): CxFunction {
        cursor.ensureKind(CXCursor_FunctionDecl)

        val tag = cursor.debugString

        return CxFunction(
            description = description,
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
        CXType_Void                  -> CxType.Void
//        CXType_Bool                  -> CxType.Bool

        CXType_Char_U, CXType_Char_S -> CxType.Number(CxNumber.Char)
        CXType_SChar                 -> CxType.Number(CxNumber.SignedChar)
        CXType_UChar                 -> CxType.Number(CxNumber.UnsignedChar)
        CXType_Short                 -> CxType.Number(CxNumber.Short)
        CXType_UShort                -> CxType.Number(CxNumber.UnsignedShort)
        CXType_Int                   -> CxType.Number(CxNumber.Int)
        CXType_UInt                  -> CxType.Number(CxNumber.UnsignedInt)
        CXType_Long                  -> CxType.Number(CxNumber.Long)
        CXType_ULong                 -> CxType.Number(CxNumber.UnsignedLong)
        CXType_LongLong              -> CxType.Number(CxNumber.LongLong)
        CXType_ULongLong             -> CxType.Number(CxNumber.UnsignedLongLong)
        CXType_Int128                -> CxType.Number(CxNumber.Int128)
        CXType_UInt128               -> CxType.Number(CxNumber.UnsignedInt128)
        CXType_Float                 -> CxType.Number(CxNumber.Float)
        // TODO: add CXType_Float16?
        CXType_Double                -> CxType.Number(CxNumber.Double)
        CXType_LongDouble            -> CxType.Number(CxNumber.LongDouble)

        // artificial type
        CXType_Elaborated            -> parseType(tag, clang_Type_getNamedType(type))

        // composite types
        CXType_Pointer               -> CxType.Pointer(parseType(tag, clang_getPointeeType(type)))
        CXType_Enum                  -> {
            visitEnum(type.cursor)
            CxType.Enum(type.cursor.usr)
        }

        CXType_Record                -> {
            visitRecord(type.cursor)
            CxType.Record(type.cursor.usr)
        }

        CXType_Typedef               -> {
            visitTypedef(type.cursor)
            CxType.Typedef(type.cursor.usr)
        }

//        CXType_FunctionProto, CXType_FunctionNoProto       -> CxType.Function(
//            returnType = parseType(tag, clang_getResultType(type)),
//            parameters = buildList {
//                repeat(clang_getNumArgTypes(type)) {
//                    add(parseType(tag, clang_getArgType(type, it.convert())))
//                }
//            }
//        )

        CXType_IncompleteArray       -> CxType.Array(
            parseType(tag, clang_getArrayElementType(type)),
            null
        )

        CXType_ConstantArray         -> CxType.Array(
            parseType(tag, clang_getArrayElementType(type)),
            clang_getArraySize(type).also {
                check(it in 0..Int.MAX_VALUE) { "Array size max value is ${Int.MAX_VALUE}, but was $it in $tag" }
            }.toInt()
        )

        // known unsupported
        CXType_Bool,
        CXType_FunctionProto,
        CXType_FunctionNoProto,
        CXType_BlockPointer,
        CXType_Vector                -> CxType.Unsupported(type.debugString)

        // unknown unsupported
        else                         -> {
            println("UNKNOWN UNSUPPORTED TYPE: ${type.debugString} in $tag")
            CxType.Unsupported(type.debugString)
        }
    }
}
