package dev.whyoleg.foreign.clang.compiler

import dev.whyoleg.foreign.clang.api.*
import dev.whyoleg.foreign.clang.compiler.libclang.*
import dev.whyoleg.foreign.clang.compiler.libclang.CXCursorKind.*
import dev.whyoleg.foreign.clang.compiler.libclang.CXTypeKind.*
import kotlinx.cinterop.*

private typealias USR = String
private typealias Path = String

internal class ClangIndexer {
    private val files = mutableMapOf<Path, CxDeclarationHeader>()

    private val visited = mutableSetOf<USR>()

    private val variables = mutableMapOf<USR, CxDeclaration<CxVariableData>>()
    private val enums = mutableMapOf<USR, CxDeclaration<CxEnumData>>()
    private val typedefs = mutableMapOf<USR, CxDeclaration<CxTypedefData>>()
    private val records = mutableMapOf<USR, CxDeclaration<CxRecordData>>()
    private val functions = mutableMapOf<USR, CxDeclaration<CxFunctionData>>()

    // temporary fields, available only during indexing of unit
    private val unnamedEnums = mutableMapOf<USR, List<CxEnumConstant>>()
    private val unnamedRecords = mutableMapOf<USR, CxRecordDefinition>()

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
                CXCursor_InclusionDirective -> {} // processed earlier
                CXCursor_VarDecl            -> visitVariable(cursor)
                CXCursor_EnumDecl           -> visitEnum(cursor)
                CXCursor_TypedefDecl        -> visitTypedef(cursor)
                CXCursor_UnionDecl,
                CXCursor_StructDecl         -> visitRecord(cursor)

                CXCursor_FunctionDecl       -> visitFunction(cursor)

                // it looks like we just should skip it
                CXCursor_UnexposedDecl      -> {}
                // skip for now
                CXCursor_MacroDefinition,
                CXCursor_MacroExpansion,
                                            -> {
                }

                else                        -> {
                    println("UNSUPPORTED CURSOR: ${cursor.debugString}")
                }
            }
        }

        // fix unnamed enums and records
        typedefs.forEach { (_, typedef) ->
            when (val type = typedef.data.aliasedType) {
                is CxType.Record -> {
                    unnamedRecords.remove(type.id)?.let { definition ->
                        records[type.id] = CxDeclaration(
                            id = type.id,
                            header = typedef.header,
                            data = CxBasicRecordData(
                                name = typedef.data.name,
                                definition = definition
                            )
                        )
                    }
                }

                is CxType.Enum   -> {
                    unnamedEnums.remove(type.id)?.let { constants ->
                        enums[type.id] = CxDeclaration(
                            id = type.id,
                            header = typedef.header,
                            CxEnumData(
                                name = typedef.data.name,
                                constants = constants
                            )
                        )
                    }
                }

                else             -> {}
            }
        }

        check(unnamedRecords.isEmpty()) {
            "there should be no unnamed records, but was: ${unnamedRecords.keys}"
        }
        check(unnamedEnums.isEmpty()) {
            "there should be no unnamed enums, but was: ${unnamedEnums.keys}"
        }
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

    private inline fun <T> MutableMap<USR, T>.save(
        cursor: CValue<CXCursor>,
        parse: (cursor: CValue<CXCursor>) -> T
    ) {
        val id = cursor.usr
        check(!containsKey(id)) { "$id is already saved" }
        put(id, parse(cursor.canonical))
    }

    private inline fun <D : CxDeclarationData> MutableMap<USR, CxDeclaration<D>>.saveDeclaration(
        cursor: CValue<CXCursor>,
        block: (cursor: CValue<CXCursor>) -> D
    ) {
        save(cursor) {
            CxDeclaration(
                id = it.usr,
                header = when (val fileName = it.location.file?.fileName) {
                    null -> {
                        println("BUILTIN: ${it.debugString}")
                        ""
                    }

                    else -> files.getValue(fileName)
                },
                data = block(it)
            )
        }
    }

    private inline fun visit(cursor: CValue<CXCursor>, block: () -> Unit) {
        if (visited.add(cursor.usr)) block()
    }

    private fun visitVariable(cursor: CValue<CXCursor>) {
        visit(cursor) {
            variables.saveDeclaration(cursor, ::parseVariable)
        }
    }

    private fun visitEnum(cursor: CValue<CXCursor>) {
        visit(cursor) {
            when {
                cursor.spelling == null && !cursor.isAnonymous -> unnamedEnums.save(cursor, ::parseEnumConstants)
                else                                           -> enums.saveDeclaration(cursor, ::parseEnum)
            }
        }
    }

    private fun visitTypedef(cursor: CValue<CXCursor>) {
        visit(cursor) {
            typedefs.saveDeclaration(cursor, ::parseTypedef)
        }
    }

    private fun visitRecord(cursor: CValue<CXCursor>) {
        visit(cursor) {
            when {
                cursor.spelling == null && !cursor.isAnonymous -> unnamedRecords.save(cursor, ::parseRecordDefinition)
                else                                           -> records.saveDeclaration(cursor, ::parseRecord)
            }
        }
    }

    private fun visitFunction(cursor: CValue<CXCursor>) {
        visit(cursor) {
            functions.saveDeclaration(cursor, ::parseFunction)
        }
    }

    private fun parseVariable(cursor: CValue<CXCursor>): CxVariableData {
        cursor.ensureKind(CXCursor_VarDecl)

        val tag = cursor.debugString

        return CxVariableData(
            name = checkNotNull(cursor.spelling) { "Variable should have a name: $tag" },
            variableType = parseType(tag, cursor.type)
        )
    }

    private fun parseEnum(cursor: CValue<CXCursor>): CxEnumData {
        cursor.ensureKind(CXCursor_EnumDecl)

        return CxEnumData(
            name = cursor.spelling,
            constants = parseEnumConstants(cursor)
        )
    }

    private fun parseEnumConstants(cursor: CValue<CXCursor>): List<CxEnumConstant> = buildList {
        val tag = cursor.debugString
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

    private fun parseTypedef(cursor: CValue<CXCursor>): CxTypedefData {
        cursor.ensureKind(CXCursor_TypedefDecl)

        val tag = cursor.debugString

        return CxTypedefData(
            name = checkNotNull(cursor.spelling) { "Typedef should have a name: $tag" },
            aliasedType = parseType(tag, clang_getTypedefDeclUnderlyingType(cursor)),
            resolvedType = parseType(tag, clang_getCanonicalType(cursor.type))
        )
    }

    private fun parseRecord(cursor: CValue<CXCursor>): CxRecordData {
        cursor.ensureKind(CXCursor_StructDecl, CXCursor_UnionDecl)

        val tag = cursor.debugString

        val definitionCursor = cursor.definition

        return when {
            definitionCursor.kind == CXCursor_InvalidFile -> CxOpaqueRecordData(
                name = checkNotNull(cursor.spelling) { "Opaque Record should have a name: $tag" }
            )

            definitionCursor.isAnonymous                  -> CxAnonymousRecordData(
                definition = parseRecordDefinition(definitionCursor)
            )

            else                                          -> CxBasicRecordData(
                name = checkNotNull(cursor.spelling) { "Basic Record should have a name: $tag" },
                definition = parseRecordDefinition(definitionCursor)
            )
        }
    }

    private fun parseRecordDefinition(cursor: CValue<CXCursor>): CxRecordDefinition {
        cursor.ensureKind(CXCursor_StructDecl, CXCursor_UnionDecl)

        val tag = cursor.debugString

        return CxRecordDefinition(
            isUnion = when (cursor.kind) {
                CXCursor_StructDecl -> false
                CXCursor_UnionDecl  -> true
                else                -> cursor.throwWrongKind()
            },
            size = clang_Type_getSizeOf(cursor.type).also { check(it > 0) { "wrong sizeOf(=$it) result: $tag" } },
            align = clang_Type_getAlignOf(cursor.type).also { check(it > 0) { "wrong alignOf(=$it) result: $tag" } },
            fields = buildList {
                cursor.visitChildren { fieldCursor ->
                    when (fieldCursor.kind) {
                        CXCursor_FieldDecl -> add(
                            CxRecordField(
                                name = fieldCursor.spelling,
                                fieldType = parseType(tag, fieldCursor.type),
                                bitWidth = clang_getFieldDeclBitWidth(fieldCursor).takeIf { it > 0 }
                            )
                        )
                        // such declarations will be parsed in parseType
                        CXCursor_StructDecl,
                        CXCursor_UnionDecl -> {
                        }
                        // TODO: how to handle this?
                        CXCursor_UnexposedAttr,
                        CXCursor_PackedAttr,
                        CXCursor_AlignedAttr
                                           -> {
                        }

                        else               -> fieldCursor.throwWrongKind()
                    }
                }
            }
        )
    }

    private fun parseFunction(cursor: CValue<CXCursor>): CxFunctionData {
        cursor.ensureKind(CXCursor_FunctionDecl)

        val tag = cursor.debugString

        return CxFunctionData(
            name = checkNotNull(cursor.spelling) { "Function should have a name: $tag" },
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
        CXType_Double                                -> CxType.Number(CxNumber.Double)
        CXType_LongDouble                            -> CxType.Number(CxNumber.LongDouble)

        // artificial type
        CXType_Elaborated                            -> parseType(tag, clang_Type_getNamedType(type))

        // composite types
        CXType_Pointer                               -> CxType.Pointer(parseType(tag, clang_getPointeeType(type)))
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
                check(it.toInt().toLong() == it) { "Array size max value is ${Int.MAX_VALUE}, but was $it in $tag" }
                it.toInt()
            }
        )

        // TODO: what to do here?
        CXType_BlockPointer                          -> CxType.Unsupported("Block pointers are not supported")
        CXType_Vector                                -> CxType.Unsupported("Vectors are not supported")

        else                                         -> {
            println("UNSUPPORTED TYPE: ${type.debugString} in $tag")
            CxType.Unsupported(type.debugString)
        }
    }
}
