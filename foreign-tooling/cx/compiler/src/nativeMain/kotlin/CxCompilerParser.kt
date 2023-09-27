package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.clang.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.CXCursorKind.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.CXTypeKind.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.CxCompilerDataType.*
import kotlinx.cinterop.*

private typealias MutableDeclarations<T> = MutableMap<CxCompilerDeclarationId, CxCompilerDeclaration<T>>

private fun <T : CxCompilerDeclarationData?> createMap(): MutableDeclarations<T> = mutableMapOf()

private const val UNNAMED = "UNNAMED"

internal class CxCompilerParser {
    private val variables = createMap<CxCompilerVariableData>()
    private val enums = createMap<CxCompilerEnumData>()
    private val records = createMap<CxCompilerRecordData?>()
    private val typedefs = createMap<CxCompilerTypedefData>()
    private val functions = createMap<CxCompilerFunctionData>()

    private val headerByPath = mutableMapOf<String, CxCompilerHeaderId>()

    fun parseTranslationUnit(mainFileName: String, translationUnit: CXTranslationUnit) {
        headerByPath[translationUnit.spelling] = CxCompilerHeaderId.Main(mainFileName)
        translationUnit.cursor.visitChildren { cursor ->
            when (cursor.kind) {
                // need to be parsed
                CXCursor_InclusionDirective -> {
                    // TODO: recheck collisions?
                    headerByPath[cursor.includedFile.fileName] =
                        CxCompilerHeaderId.Included(cursor.spelling ?: error("Include header is blank: ${cursor.debugString}"))
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
        val unnamedEnums = enums.filterValues { it.declarationName == UNNAMED }
        val unnamedRecords = records.filterValues { it.declarationName == UNNAMED }
        typedefs.values.forEach { typedef ->
            when (val type = typedef.data.aliasedType) {
                is Enum             -> unnamedEnums[type.id]?.let {
                    enums[it.id] = it.copy(declarationName = typedef.declarationName)
                }
                is Record.Reference -> unnamedRecords[type.id]?.let {
                    records[it.id] = it.copy(declarationName = typedef.declarationName)
                }
                else                -> {}
            }
        }
        enums.filterValues { it.declarationName == UNNAMED }.also {
            check(it.isEmpty()) { "unnamed enums: ${it.keys}" }
        }
        records.filterValues { it.declarationName == UNNAMED }.also {
            check(it.isEmpty()) { "unnamed records: ${it.keys}" }
        }
    }

    fun getIndex(): CxCompilerIndex {
        assertAllDeclarationsAccessible()

        return CxCompilerIndex(
            variables = variables.toMap(),
            enums = enums.toMap(),
            records = records.toMap(),
            typedefs = typedefs.toMap(),
            functions = functions.toMap(),
        )
    }

    private fun parseVariable(cursor: CValue<CXCursor>) = parseDeclaration(variables, cursor, ::parseVariableData)
    private fun parseEnum(cursor: CValue<CXCursor>) = parseDeclaration(enums, cursor, ::parseEnumData)
    private fun parseRecord(cursor: CValue<CXCursor>) = parseDeclaration(records, cursor) {
        val definitionCursor = cursor.definition
        // if there is no definition, no data could be parsed
        if (definitionCursor.kind.isInvalid) null else parseRecordData(definitionCursor)
    }

    private fun parseTypedef(cursor: CValue<CXCursor>) = parseDeclaration(typedefs, cursor, ::parseTypedefData)
    private fun parseFunction(cursor: CValue<CXCursor>) = parseDeclaration(functions, cursor, ::parseFunctionData)

    private fun <T : CxCompilerDeclarationData?> parseDeclaration(
        declarations: MutableDeclarations<T>,
        cursor: CValue<CXCursor>,
        block: (cursor: CValue<CXCursor>) -> T,
    ): CxCompilerDeclarationId {
        // if enum cursor isAnonymous - it's just a declaration of variables, and we should allow it
        check(!cursor.isAnonymous || cursor.kind == CXCursor_EnumDecl) { "cursor couldn't be anonymous here: ${cursor.spelling}" }

        val id = CxCompilerDeclarationId(cursor.usr)

        if (id in declarations) return id

        declarations[id] = CxCompilerDeclaration(
            id = id,
            declarationName = when (cursor.kind) {
                CXCursor_EnumDecl,
                CXCursor_StructDecl,
                CXCursor_UnionDecl -> cursor.spelling ?: UNNAMED
                else               -> checkNotNull(cursor.spelling) { "Declaration should have name: ${cursor.debugString}" }
            },
            headerId = when (val fileName = cursor.canonical.location.file?.fileName) {
                null -> CxCompilerHeaderId.Builtin
                else -> headerByPath.getValue(fileName)
            },
            data = try {
                // hack for recursive records
                @Suppress("UNCHECKED_CAST")
                (declarations as MutableMap<CxCompilerDeclarationId, Any>)[id] = Unit
                block(cursor)
            } catch (cause: Throwable) {
                declarations -= id
                throw cause
            }
        )

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
        CXType_Record          -> {
            val cursor = type.cursor
            when {
                cursor.isAnonymous -> Record.Anonymous(parseRecordData(cursor))
                else               -> Record.Reference(parseRecord(cursor))
            }
        }
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

    private fun parseVariableData(cursor: CValue<CXCursor>): CxCompilerVariableData {
        cursor.ensureKind(CXCursor_VarDecl)

        val tag = cursor.debugString

        return CxCompilerVariableData(
            returnType = parseType(tag, cursor.type)
        )
    }

    private fun parseEnumData(cursor: CValue<CXCursor>): CxCompilerEnumData {
        cursor.ensureKind(CXCursor_EnumDecl)

        return CxCompilerEnumData(
            unnamed = cursor.isAnonymous,
            constants = buildList {
                cursor.visitChildren { constantCursor ->
                    constantCursor.ensureKind(CXCursor_EnumConstantDecl)

                    add(
                        CxCompilerEnumData.Constant(
                            name = constantCursor.spelling
                                ?: error("Enum constant is blank: ${constantCursor.debugString} in ${cursor.debugString}"),
                            value = clang_getEnumConstantDeclValue(constantCursor)
                        )
                    )
                }
            }
        )
    }

    private fun parseRecordData(cursor: CValue<CXCursor>): CxCompilerRecordData {
        check(cursor.isDefinition) { "Cursor for record should be definition cursor: ${cursor.debugString}" }

        val isUnion = when (cursor.kind) {
            CXCursor_StructDecl -> false
            CXCursor_UnionDecl  -> true
            else                -> cursor.throwWrongKind()
        }

        val tag = cursor.debugString

        // more info on size values https://clang.llvm.org/doxygen/group__CINDEX__TYPES.html#gaaf1b95e9e7e792a08654563fef7502c1
        val size = clang_Type_getSizeOf(cursor.type)
        val align = clang_Type_getAlignOf(cursor.type)

        check(size > 0 && align > 0) { "wrong sizeOf(=$size) or alignOf(=$align) result: ${cursor.debugString}" }

        val fields = buildList {
            cursor.visitChildren { fieldCursor ->
                when (fieldCursor.kind) {
                    CXCursor_FieldDecl  -> {
                        add(
                            CxCompilerRecordData.Field(
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

        return CxCompilerRecordData(isUnion, size, align, fields)
    }

    private fun parseTypedefData(cursor: CValue<CXCursor>): CxCompilerTypedefData {
        cursor.ensureKind(CXCursor_TypedefDecl)

        val tag = cursor.debugString

        return CxCompilerTypedefData(
            aliasedType = parseType(tag, clang_getTypedefDeclUnderlyingType(cursor)),
            resolvedType = parseType(tag, clang_getCanonicalType(cursor.type))
        )
    }

    private fun parseFunctionData(cursor: CValue<CXCursor>): CxCompilerFunctionData {
        cursor.ensureKind(CXCursor_FunctionDecl)

        val tag = cursor.debugString

        return CxCompilerFunctionData(
            isVariadic = clang_isFunctionTypeVariadic(cursor.type) > 0U,
            returnType = parseType(tag, clang_getCursorResultType(cursor)),
            parameters = buildList {
                cursor.forEachArgument { argCursor ->
                    argCursor.ensureKind(CXCursor_ParmDecl)

                    add(
                        CxCompilerFunctionData.Parameter(
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
                is Pointer          -> pointed.collectIds()
                is Array            -> elementType.collectIds()
                is Function         -> {
                    returnType.collectIds()
                    parameters.forEach { it.collectIds() }
                }
                is Enum             -> enumIds.add(id)
                is Record.Reference -> recordIds.add(id)
                is Typedef          -> typedefIds.add(id)

                is Record.Anonymous,
                is Primitive,
                is Unsupported      -> {
                }
            }
        }

        variables.values.forEach {
            it.data.returnType.collectIds()
        }
        typedefs.values.forEach {
            it.data.aliasedType.collectIds()
            it.data.resolvedType.collectIds()
        }
        records.values.forEach {
            it.data?.fields?.forEach { it.type.collectIds() }
        }
        functions.values.forEach {
            it.data.returnType.collectIds()
            it.data.parameters.forEach { it.type.collectIds() }
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
