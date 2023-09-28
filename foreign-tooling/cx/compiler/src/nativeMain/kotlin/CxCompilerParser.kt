package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.clang.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.CXCursorKind.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.CXTypeKind.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.CxCompilerDataType.*
import kotlinx.cinterop.*

internal class CxCompilerParser {
    private val variables = mutableMapOf<String, CxCompilerVariable>()
    private val enums = mutableMapOf<String, CxCompilerEnum>()
    private val records = mutableMapOf<String, CxCompilerRecord>()
    private val typedefs = mutableMapOf<String, CxCompilerTypedef>()
    private val functions = mutableMapOf<String, CxCompilerFunction>()

    private val headerByPath = mutableMapOf<String, CxCompilerFileId>()

    fun parseTranslationUnit(mainFileName: String, translationUnit: CXTranslationUnit) {
        headerByPath[translationUnit.spelling] = CxCompilerFileId.Main(mainFileName)
        translationUnit.cursor.visitChildren { cursor ->
            when (cursor.kind) {
                // need to be parsed
                CXCursor_InclusionDirective -> {
                    // TODO: recheck collisions?
                    headerByPath[cursor.includedFile.fileName] =
                        CxCompilerFileId.Included(cursor.spelling ?: error("Include header is blank: ${cursor.debugString}"))
                }
                CXCursor_TypedefDecl        -> parseTypedef(cursor)
                CXCursor_FunctionDecl       -> parseFunction(cursor)
                CXCursor_EnumDecl           -> parseEnum(cursor)
                CXCursor_UnionDecl,
                CXCursor_StructDecl         -> parseRecord(cursor)
                CXCursor_VarDecl            -> parseVariable(cursor)
                // it looks like we just should skip it
                CXCursor_UnexposedDecl      -> {}
                // skip for now
                CXCursor_MacroDefinition,
                CXCursor_MacroExpansion,
                                            -> {
                }

                else                        -> println("Not supported: ${cursor.debugString}")
            }
        }

        // fix unnamed declarations, when enum/record has no name, but there is declared typedef, f.e.:
        //   typedef union {} XXX;
        val unnamedEnums = enums.filterValues { it.name == null }
        val unnamedRecords = records.filterValues { it.name == null }
        typedefs.values.forEach { typedef ->
            when (val type = typedef.aliasedType) {
                is Enum   -> unnamedEnums[type.id.value]?.let {
                    enums[type.id.value] = it.copy(name = typedef.name)
                }
                is Record -> unnamedRecords[type.id.value]?.let {
                    records[type.id.value] = it.copy(name = typedef.name)
                }
                else      -> {}
            }
        }
    }

    fun getIndex(): CxCompilerIndex {
        assertAllDeclarationsAccessible()

        return CxCompilerIndex(
            variables = variables.values.toList(),
            enums = enums.values.toList(),
            records = records.values.toList(),
            typedefs = typedefs.values.toList(),
            functions = functions.values.toList(),
        )
    }

    private fun <T : CxCompilerDeclaration> parseDeclaration(
        declarations: MutableMap<String, T>,
        cursor: CValue<CXCursor>,
        block: (
            id: CxCompilerDeclarationId,
            fileId: CxCompilerFileId,
            name: String?,
            cursor: CValue<CXCursor>
        ) -> T,
    ): CxCompilerDeclarationId {
        val usr = cursor.usr
        val id = CxCompilerDeclarationId(cursor.usr)

        if (usr in declarations) return id
        val fileId = when (val fileName = cursor.canonical.location.file?.fileName) {
            null -> CxCompilerFileId.Builtin
            else -> headerByPath.getValue(fileName)
        }
        val declarationName = cursor.spelling
        declarations[usr] = try {
            // hack for recursive records
            @Suppress("UNCHECKED_CAST")
            (declarations as MutableMap<String, Any>)[usr] = Unit
            block(id, fileId, declarationName, cursor)
        } catch (cause: Throwable) {
            declarations -= usr
            throw cause
        }

        return id
    }

    private fun parseType(tag: String, type: CValue<CXType>): CxCompilerDataType = when (type.kind) {
        CXType_Void            -> Primitive(CxPrimitiveDataType.Void)
        CXType_Bool            -> Primitive(CxPrimitiveDataType.Bool)
        CXType_Char_U,
        CXType_Char_S          -> Primitive(CxPrimitiveDataType.Char)
        CXType_SChar           -> Primitive(CxPrimitiveDataType.SignedChar)
        CXType_UChar           -> Primitive(CxPrimitiveDataType.UnsignedChar)
        CXType_Short           -> Primitive(CxPrimitiveDataType.Short)
        CXType_UShort          -> Primitive(CxPrimitiveDataType.UnsignedShort)
        CXType_Int             -> Primitive(CxPrimitiveDataType.Int)
        CXType_UInt            -> Primitive(CxPrimitiveDataType.UnsignedInt)
        CXType_Long            -> Primitive(CxPrimitiveDataType.Long)
        CXType_ULong           -> Primitive(CxPrimitiveDataType.UnsignedLong)
        CXType_LongLong        -> Primitive(CxPrimitiveDataType.LongLong)
        CXType_ULongLong       -> Primitive(CxPrimitiveDataType.UnsignedLongLong)
        CXType_Int128          -> Primitive(CxPrimitiveDataType.Int128)
        CXType_UInt128         -> Primitive(CxPrimitiveDataType.UnsignedInt128)
        CXType_Float           -> Primitive(CxPrimitiveDataType.Float)
        CXType_Double          -> Primitive(CxPrimitiveDataType.Double)
        CXType_LongDouble      -> Primitive(CxPrimitiveDataType.LongDouble)

        // artificial type
        CXType_Elaborated      -> parseType(tag, clang_Type_getNamedType(type))

        // composite types
        CXType_Pointer         -> Pointer(parseType(tag, clang_getPointeeType(type)))
        CXType_Enum            -> Enum(parseEnum(type.cursor))
        CXType_Typedef         -> Typedef(parseTypedef(type.cursor))
        CXType_Record -> Record(parseRecord(type.cursor))
        CXType_FunctionProto   -> Function(
            returnType = parseType(tag, clang_getResultType(type)),
            parameters = buildList {
                repeat(clang_getNumArgTypes(type)) {
                    add(parseType(tag, clang_getArgType(type, it.convert())))
                }
            }
        )
        CXType_IncompleteArray -> IncompleteArray(parseType(tag, clang_getArrayElementType(type)))
        CXType_ConstantArray   -> ConstArray(parseType(tag, clang_getArrayElementType(type)), clang_getArraySize(type))
        //println("UNSUPPORTED TYPE: ${type.debugString} in $tag")
        else                   -> Unsupported(type.debugString)
    }


    private fun parseVariable(cursor: CValue<CXCursor>) = parseDeclaration(variables, cursor, ::parseVariableData)
    private fun parseVariableData(
        id: CxCompilerDeclarationId,
        fileId: CxCompilerFileId,
        name: String?,
        cursor: CValue<CXCursor>
    ): CxCompilerVariable {
        cursor.ensureKind(CXCursor_VarDecl)

        val tag = cursor.debugString

        return CxCompilerVariable(
            id = id,
            fileId = fileId,
            name = checkNotNull(name) { "Variable should have a name: $tag" },
            type = parseType(tag, cursor.type)
        )
    }

    private fun parseEnum(cursor: CValue<CXCursor>) = parseDeclaration(enums, cursor, ::parseEnumData)
    private fun parseEnumData(
        id: CxCompilerDeclarationId,
        fileId: CxCompilerFileId,
        name: String?,
        cursor: CValue<CXCursor>
    ): CxCompilerEnum {
        cursor.ensureKind(CXCursor_EnumDecl)

        return CxCompilerEnum(
            id = id,
            fileId = fileId,
            name = name,
            constants = buildList {
                cursor.visitChildren { constantCursor ->
                    constantCursor.ensureKind(CXCursor_EnumConstantDecl)

                    add(
                        CxCompilerEnum.Constant(
                            name = constantCursor.spelling
                                ?: error("Enum constant is blank: ${constantCursor.debugString} in ${cursor.debugString}"),
                            value = clang_getEnumConstantDeclValue(constantCursor)
                        )
                    )
                }
            }
        )
    }

    private fun parseRecord(cursor: CValue<CXCursor>) = parseDeclaration(records, cursor, ::parseRecordData)
    private fun parseRecordData(
        id: CxCompilerDeclarationId,
        fileId: CxCompilerFileId,
        name: String?,
        originalCursor: CValue<CXCursor>
    ): CxCompilerRecord {
        val cursor = originalCursor.definition
        val tag = cursor.debugString
        return CxCompilerRecord(
            id = id,
            fileId = fileId,
            name = name,
            isUnion = when (originalCursor.kind) {
                CXCursor_StructDecl -> false
                CXCursor_UnionDecl  -> true
                else                -> originalCursor.throwWrongKind()
            },
            definition = when {
                // if there is no definition, no data could be parsed
                cursor.kind.isInvalid -> null
                else                  -> CxCompilerRecord.Definition(
                    size = clang_Type_getSizeOf(cursor.type).also { check(it > 0) { "wrong sizeOf(=$it) result: ${cursor.debugString}" } },
                    align = clang_Type_getAlignOf(cursor.type).also { check(it > 0) { "wrong alignOf(=$it) result: ${cursor.debugString}" } },
                    fields = buildList {
                        cursor.visitChildren { fieldCursor ->
                            when (fieldCursor.kind) {
                                CXCursor_FieldDecl  -> {
                                    add(
                                        CxCompilerRecord.Field(
                                            name = fieldCursor.spelling,
                                            type = parseType(tag, fieldCursor.type),
                                            bitWidth = clang_getFieldDeclBitWidth(fieldCursor).takeIf { it > 0 }
                                        )
                                    )
                                }
                                // such declarations will be parsed in parseType
                                CXCursor_StructDecl,
                                CXCursor_UnionDecl  -> {
                                }
                                // TODO: how to handle this?
                                CXCursor_UnexposedAttr,
                                CXCursor_PackedAttr -> {
                                }
                                else                -> fieldCursor.throwWrongKind()
                            }
                        }
                    }
                )
            }
        )
    }

    private fun parseTypedef(cursor: CValue<CXCursor>) = parseDeclaration(typedefs, cursor, ::parseTypedefData)
    private fun parseTypedefData(
        id: CxCompilerDeclarationId,
        fileId: CxCompilerFileId,
        name: String?,
        cursor: CValue<CXCursor>
    ): CxCompilerTypedef {
        cursor.ensureKind(CXCursor_TypedefDecl)

        val tag = cursor.debugString

        return CxCompilerTypedef(
            id = id,
            fileId = fileId,
            name = checkNotNull(name) { "Typedef should have a name: $tag" },
            aliasedType = parseType(tag, clang_getTypedefDeclUnderlyingType(cursor)),
            resolvedType = parseType(tag, clang_getCanonicalType(cursor.type))
        )
    }

    private fun parseFunction(cursor: CValue<CXCursor>) = parseDeclaration(functions, cursor, ::parseFunctionData)
    private fun parseFunctionData(
        id: CxCompilerDeclarationId,
        fileId: CxCompilerFileId,
        name: String?,
        cursor: CValue<CXCursor>
    ): CxCompilerFunction {
        cursor.ensureKind(CXCursor_FunctionDecl)

        val tag = cursor.debugString

        return CxCompilerFunction(
            id = id,
            fileId = fileId,
            name = checkNotNull(name) { "Function should have a name: $tag" },
            isVariadic = clang_isFunctionTypeVariadic(cursor.type) > 0U,
            returnType = parseType(tag, clang_getCursorResultType(cursor)),
            parameters = buildList {
                cursor.forEachArgument { argCursor ->
                    argCursor.ensureKind(CXCursor_ParmDecl)

                    add(
                        CxCompilerFunction.Parameter(
                            name = argCursor.spelling,
                            type = parseType(tag, argCursor.type)
                        )
                    )
                }
            }
        )
    }

    // TODO: is it really needed?
    private fun assertAllDeclarationsAccessible() {
        val typedefIds = typedefs.keys.toMutableSet()
        val recordIds = records.keys.toMutableSet()
        val enumIds = enums.keys.toMutableSet()

        fun CxCompilerDataType.collectIds() {
            when (this) {
                is Pointer     -> pointed.collectIds()
                is Array       -> elementType.collectIds()
                is Function    -> {
                    returnType.collectIds()
                    parameters.forEach { it.collectIds() }
                }
                is Enum        -> enumIds.add(id.value)
                is Record      -> recordIds.add(id.value)
                is Typedef     -> typedefIds.add(id.value)
                is Primitive,
                is Unsupported -> {
                }
            }
        }

        variables.values.forEach {
            it.type.collectIds()
        }
        typedefs.values.forEach {
            it.aliasedType.collectIds()
            it.resolvedType.collectIds()
        }
        records.values.forEach {
            it.definition?.fields?.forEach { it.type.collectIds() }
        }
        functions.values.forEach {
            it.returnType.collectIds()
            it.parameters.forEach { it.type.collectIds() }
        }

        fun stats(tag: String, expected: Set<*>, actual: Map<*, *>) {
            check(
                expected.subtract(actual.keys).isEmpty() &&
                        actual.keys.subtract(expected).isEmpty()
            ) {
                """|$tag missing:
                   |  expected   : ${expected.size}
                   |  actual     : ${actual.size}
                   |  intersect 1: ${expected.intersect(actual.keys).size}
                   |  intersect 2: ${actual.keys.intersect(expected).size}
                   |  subtract  1: ${expected.subtract(actual.keys)}
                   |  subtract  2: ${actual.keys.subtract(expected)}
                """.trimMargin()
            }
        }

        stats("Typedefs", typedefIds, typedefs)
        stats("Records", recordIds, records)
        stats("Enum", enumIds, enums)
    }

}
