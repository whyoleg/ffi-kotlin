package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.clang.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.CXCursorKind.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.CXTypeKind.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import dev.whyoleg.foreign.tooling.cx.compiler.test.support.*
import kotlin.test.*

class PrimitivesTest {

    @Test
    fun test() {
        listOf(
            CxCompilerTarget.MacosArm64,
            CxCompilerTarget.MacosX64,
            CxCompilerTarget.MingwX64,
            CxCompilerTarget.LinuxX64,
            CxCompilerTarget.IosDeviceArm64,
            CxCompilerTarget.IosSimulatorArm64,
            CxCompilerTarget.IosSimulatorX64,
        ).forEach { target ->
            val types = mutableMapOf(
                CxPrimitiveDataType.Void to 0,
                CxPrimitiveDataType.Bool to Byte.SIZE_BYTES
            )
            useIndex { index ->
                useTranslationUnit(
                    index,
                    "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/foreign-tooling/cx/compiler/src/nativeTest/resources/primitives.h",
                    compilerArgs(target)
                ) { translationUnit ->
                    translationUnit.cursor.visitChildren { cursor ->
                        if (cursor.spelling == "primitives") {
                            cursor.ensureKind(CXCursor_StructDecl)
                            cursor.visitChildren { fieldCursor ->
                                fieldCursor.ensureKind(CXCursor_FieldDecl)

                                val type = when (val typeKind = fieldCursor.type.kind) {
                                    CXType_Char_U,
                                    CXType_Char_S     -> CxPrimitiveDataType.Char
                                    CXType_SChar      -> CxPrimitiveDataType.SignedChar
                                    CXType_UChar      -> CxPrimitiveDataType.UnsignedChar
                                    CXType_Short      -> CxPrimitiveDataType.Short
                                    CXType_UShort     -> CxPrimitiveDataType.UnsignedShort
                                    CXType_Int        -> CxPrimitiveDataType.Int
                                    CXType_UInt       -> CxPrimitiveDataType.UnsignedInt
                                    CXType_Long       -> CxPrimitiveDataType.Long
                                    CXType_ULong      -> CxPrimitiveDataType.UnsignedLong
                                    CXType_LongLong   -> CxPrimitiveDataType.LongLong
                                    CXType_ULongLong  -> CxPrimitiveDataType.UnsignedLongLong
                                    CXType_Int128     -> CxPrimitiveDataType.Int128
                                    CXType_UInt128    -> CxPrimitiveDataType.UnsignedInt128
                                    CXType_Float      -> CxPrimitiveDataType.Float
                                    CXType_Double     -> CxPrimitiveDataType.Double
                                    CXType_LongDouble -> CxPrimitiveDataType.LongDouble
                                    else              -> error("wrong type: $typeKind")
                                }
                                val typeSize = clang_Type_getSizeOf(fieldCursor.type).toInt()
                                check(types.put(type, typeSize) == null) { "$type already registered" }
                            }
                            println(cursor.debugString)
                        }
                    }
                }
            }
            assertEquals(types, target.primitiveDataTypeSizes, target.toString())
        }
    }
}
