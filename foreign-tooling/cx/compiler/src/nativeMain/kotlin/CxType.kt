package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.internal.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.CXTypeKind.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import kotlinx.cinterop.*

internal fun CxIndexBuilder.buildType(type: CValue<CXType>): CxType = when (val kind = type.kind) {
    CXType_Void            -> CxType.Void
    CXType_Bool            -> CxType.Boolean
    CXType_Char_U,
    CXType_Char_S          -> CxType.Builtin(CxBuiltinType.Char)
    CXType_SChar           -> CxType.Builtin(CxBuiltinType.SignedChar)
    CXType_UChar           -> CxType.Builtin(CxBuiltinType.UnsignedChar)
    CXType_Short           -> CxType.Builtin(CxBuiltinType.Short)
    CXType_UShort          -> CxType.Builtin(CxBuiltinType.UnsignedShort)
    CXType_Int             -> CxType.Builtin(CxBuiltinType.Int)
    CXType_UInt            -> CxType.Builtin(CxBuiltinType.UnsignedInt)
    CXType_Long            -> CxType.Builtin(CxBuiltinType.Long)
    CXType_ULong           -> CxType.Builtin(CxBuiltinType.UnsignedLong)
    CXType_LongLong        -> CxType.Builtin(CxBuiltinType.LongLong)
    CXType_ULongLong       -> CxType.Builtin(CxBuiltinType.UnsignedLongLong)
    CXType_Int128          -> CxType.Builtin(CxBuiltinType.Int128)
    CXType_UInt128         -> CxType.Builtin(CxBuiltinType.UnsignedInt128)
    CXType_Float           -> CxType.Builtin(CxBuiltinType.Float)
    CXType_Double          -> CxType.Builtin(CxBuiltinType.Double)
    CXType_LongDouble      -> CxType.Builtin(CxBuiltinType.LongDouble)

    CXType_Elaborated      -> buildType(clang_Type_getNamedType(type))

    CXType_Pointer         -> CxType.Pointer(buildType(clang_getPointeeType(type)))
    CXType_Enum            -> CxType.Enum(enum(clang_getTypeDeclaration(type)))
    CXType_Typedef         -> CxType.Typedef(typedef(clang_getTypeDeclaration(type)))
    CXType_Record          -> CxType.Record(record(clang_getTypeDeclaration(type)))
    CXType_FunctionProto   -> CxType.Function(
        returnType = buildType(clang_getResultType(type)),
        parameters = buildList {
            repeat(clang_getNumArgTypes(type)) {
                add(buildType(clang_getArgType(type, it.convert())))
            }
        }
    )
    CXType_IncompleteArray -> CxType.IncompleteArray(buildType(clang_getArrayElementType(type)))
    CXType_ConstantArray   -> CxType.ConstArray(buildType(clang_getArrayElementType(type)), clang_getArraySize(type))
    CXType_BlockPointer    -> CxType.Unknown(type.spelling, type.kind.toString()) //TODO?
    CXType_Unexposed       -> {
        check(clang_getResultType(type).kind == CXType_Invalid)
        val canonicalType = clang_getCanonicalType(type)
        when (canonicalType.kind) {
            CXType_Unexposed -> CxType.Unknown(canonicalType.spelling, canonicalType.kind.toString())
            else             -> buildType(canonicalType)
        }
    }
    else                   -> TODO("NOT SUPPORTED: $kind | ${type.spelling}")
}
