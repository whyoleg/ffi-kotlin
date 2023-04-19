package dev.whyoleg.foreign.cx.index.cli.info

import dev.whyoleg.foreign.cx.index.*
import dev.whyoleg.foreign.cx.index.clang.*
import dev.whyoleg.foreign.cx.index.clang.CXTypeKind.*
import dev.whyoleg.foreign.cx.index.cli.*
import dev.whyoleg.foreign.cx.index.cli.internal.*
import kotlinx.cinterop.*

fun CxIndexBuilder.buildTypeInfo(
    type: CValue<CXType>,
): CxTypeInfo = CxTypeInfo(
    name = type.spelling,
    type = buildType(type)
)

private fun CxIndexBuilder.buildType(
    type: CValue<CXType>,
): CxType = when (val kind = type.kind) {
    CXType_Void -> CxType.Void
    CXType_Bool -> CxType.Boolean
    CXType_Char_U, CXType_Char_S -> CxType.Char
    CXType_UChar -> CxType.UByte
    CXType_SChar -> CxType.Byte
    CXType_UShort -> CxType.UShort
    CXType_UInt -> CxType.UInt
    CXType_ULong -> CxType.ULong
    CXType_ULongLong -> CxType.ULongLong
    CXType_Short -> CxType.Short
    CXType_Int -> CxType.Int
    CXType_Long -> CxType.Long
    CXType_LongLong -> CxType.LongLong
    CXType_Float -> CxType.Float
    CXType_Double -> CxType.Double

    CXType_LongDouble -> CxType.LongDouble
    CXType_Int128 -> CxType.Int128
    CXType_UInt128 -> CxType.UInt128

    CXType_Elaborated -> buildType(clang_Type_getNamedType(type))

    CXType_Pointer -> CxType.Pointer(buildType(clang_getPointeeType(type)))
    CXType_Enum -> CxType.Enum(enum(clang_getTypeDeclaration(type)))
    CXType_Typedef -> CxType.Typedef(typedef(clang_getTypeDeclaration(type)))
    CXType_Record -> CxType.Struct(struct(clang_getTypeDeclaration(type)))
    CXType_FunctionProto -> CxType.Function(
        returnType = buildType(clang_getResultType(type)),
        parameters = buildList {
            repeat(clang_getNumArgTypes(type)) {
                add(buildType(clang_getArgType(type, it.convert())))
            }
        }
    )
    CXType_IncompleteArray -> CxType.IncompleteArray(buildType(clang_getArrayElementType(type)))
    CXType_ConstantArray -> CxType.ConstArray(buildType(clang_getArrayElementType(type)), clang_getArraySize(type))
    CXType_BlockPointer -> CxType.Unknown(type.spelling, type.kind.toString()) //TODO?
    CXType_Unexposed -> {
        check(clang_getResultType(type).kind == CXType_Invalid)
        val canonicalType = clang_getCanonicalType(type)
        when (canonicalType.kind) {
            CXType_Unexposed -> CxType.Unknown(canonicalType.spelling, canonicalType.kind.toString())
            else             -> buildType(canonicalType)
        }
    }
    else -> TODO("NOT SUPPORTED: $kind | ${type.spelling}")
}

private val CValue<CXType>.spelling: String get() = clang_getTypeSpelling(this).useString()!!
private val CValue<CXType>.kind: CXTypeKind get() = useContents { kind }