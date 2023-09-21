package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.internal.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.CXTypeKind.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import kotlinx.cinterop.*

internal fun CxCompilerIndexBuilder.buildType(type: CValue<CXType>): CxCompilerDataType = when (val kind = type.kind) {
    CXType_Void            -> CxCompilerDataType.Primitive(CxPrimitiveDataType.Void)
    CXType_Bool            -> CxCompilerDataType.Primitive(CxPrimitiveDataType.Bool)
    CXType_Char_U,
    CXType_Char_S          -> CxCompilerDataType.Primitive(CxPrimitiveDataType.Char)
    CXType_SChar           -> CxCompilerDataType.Primitive(CxPrimitiveDataType.SignedChar)
    CXType_UChar           -> CxCompilerDataType.Primitive(CxPrimitiveDataType.UnsignedChar)
    CXType_Short           -> CxCompilerDataType.Primitive(CxPrimitiveDataType.Short)
    CXType_UShort          -> CxCompilerDataType.Primitive(CxPrimitiveDataType.UnsignedShort)
    CXType_Int             -> CxCompilerDataType.Primitive(CxPrimitiveDataType.Int)
    CXType_UInt            -> CxCompilerDataType.Primitive(CxPrimitiveDataType.UnsignedInt)
    CXType_Long            -> CxCompilerDataType.Primitive(CxPrimitiveDataType.Long)
    CXType_ULong           -> CxCompilerDataType.Primitive(CxPrimitiveDataType.UnsignedLong)
    CXType_LongLong        -> CxCompilerDataType.Primitive(CxPrimitiveDataType.LongLong)
    CXType_ULongLong       -> CxCompilerDataType.Primitive(CxPrimitiveDataType.UnsignedLongLong)
    CXType_Int128          -> CxCompilerDataType.Primitive(CxPrimitiveDataType.Int128)
    CXType_UInt128         -> CxCompilerDataType.Primitive(CxPrimitiveDataType.UnsignedInt128)
    CXType_Float           -> CxCompilerDataType.Primitive(CxPrimitiveDataType.Float)
    CXType_Double          -> CxCompilerDataType.Primitive(CxPrimitiveDataType.Double)
    CXType_LongDouble      -> CxCompilerDataType.Primitive(CxPrimitiveDataType.LongDouble)

    CXType_Elaborated      -> buildType(clang_Type_getNamedType(type))

    CXType_Pointer         -> CxCompilerDataType.Pointer(buildType(clang_getPointeeType(type)))
    CXType_Enum            -> CxCompilerDataType.Enum(enum(clang_getTypeDeclaration(type)))
    CXType_Typedef         -> CxCompilerDataType.Typedef(typedef(clang_getTypeDeclaration(type)))
    CXType_Record          -> CxCompilerDataType.Record(record(clang_getTypeDeclaration(type)))
    CXType_FunctionProto   -> CxCompilerDataType.Function(
        returnType = buildType(clang_getResultType(type)),
        parameters = buildList {
            repeat(clang_getNumArgTypes(type)) {
                add(buildType(clang_getArgType(type, it.convert())))
            }
        }
    )
    CXType_IncompleteArray -> CxCompilerDataType.IncompleteArray(buildType(clang_getArrayElementType(type)))
    CXType_ConstantArray   -> CxCompilerDataType.ConstArray(buildType(clang_getArrayElementType(type)), clang_getArraySize(type))
    CXType_Unexposed       -> {
        check(clang_getResultType(type).kind == CXType_Invalid)
        val canonicalType = clang_getCanonicalType(type)
        when (canonicalType.kind) {
            CXType_Unexposed -> CxCompilerDataType.Unsupported(canonicalType.spelling, canonicalType.kind.toString())
            else             -> buildType(canonicalType)
        }
    }
    else                   -> CxCompilerDataType.Unsupported(type.spelling, type.kind.toString())
}
