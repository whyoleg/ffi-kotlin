package dev.whyoleg.ffi.c.index

import dev.whyoleg.ffi.c.index.clang.*
import dev.whyoleg.ffi.c.index.clang.CXTypeKind.*
import kotlinx.cinterop.*

public data class CTypeInfo
internal constructor(
    val cName: String?,
    val type: CType,
)

internal fun CIndexRegistry.parseTypeInfo(
    type: CValue<CXType>,
): CTypeInfo = Logger.logging("TYPE ROOT", "${type.spelling} / ${type.kind}", skipLogging = true) {
    CTypeInfo(
        cName = type.spelling,
        type = parseType(type)
    )
}

private fun CIndexRegistry.parseType(
    type: CValue<CXType>,
): CType = Logger.logging("TYPE", "${type.spelling} / ${type.kind}", skipLogging = true) {
    when (val kind = type.kind) {
        CXType_Void -> CType.Void
        CXType_Bool -> CType.Boolean
        CXType_Char_U, CXType_Char_S -> CType.Char
        CXType_UChar -> CType.UByte
        CXType_SChar -> CType.Byte
        CXType_UShort -> CType.UShort
        CXType_UInt -> CType.UInt
        CXType_ULong -> CType.ULong
        CXType_ULongLong -> CType.ULongLong
        CXType_Short -> CType.Short
        CXType_Int -> CType.Int
        CXType_Long -> CType.Long
        CXType_LongLong -> CType.LongLong
        CXType_Float -> CType.Float
        CXType_Double -> CType.Double

        CXType_LongDouble -> CType.LongDouble
        CXType_Int128 -> CType.Int128
        CXType_UInt128 -> CType.UInt128

        CXType_Elaborated -> parseType(clang_Type_getNamedType(type))

        CXType_Pointer -> CType.Pointer(parseType(clang_getPointeeType(type)))
        CXType_Enum -> CType.Enum(enum(clang_getTypeDeclaration(type)))
        CXType_Typedef -> CType.Typedef(typedef(clang_getTypeDeclaration(type)))
        CXType_Record -> CType.Struct(struct(clang_getTypeDeclaration(type)))
        CXType_FunctionProto -> CType.Function(
            returnType = parseType(clang_getResultType(type)),
            parameters = buildList {
                repeat(clang_getNumArgTypes(type)) {
                    add(parseType(clang_getArgType(type, it.convert())))
                }
            }
        )
        CXType_IncompleteArray -> CType.IncompleteArray(parseType(clang_getArrayElementType(type)))
        CXType_ConstantArray -> CType.ConstArray(parseType(clang_getArrayElementType(type)), clang_getArraySize(type))
        CXType_BlockPointer -> CType.Unknown //TODO?
        else -> TODO("NOT SUPPORTED: $kind | ${type.spelling}")
    }
}

private val CValue<CXType>.spelling: String? get() = clang_getTypeSpelling(this).useString()
private val CValue<CXType>.kind: CXTypeKind get() = useContents { kind }
