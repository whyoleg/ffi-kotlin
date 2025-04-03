package dev.whyoleg.foreign.tool.clang

import dev.whyoleg.foreign.tool.libclang.*
import dev.whyoleg.foreign.tool.libclang.CXCursorKind.*
import dev.whyoleg.foreign.tool.libclang.CXTypeKind.*
import dev.whyoleg.foreign.tool.clang.api.*
import dev.whyoleg.foreign.tool.clang.testenv.*
import kotlinx.io.files.*
import kotlin.test.*

class PrimitivesTest {

    @Test
    fun test() {
        val headersPath = SystemFileSystem.resolve(Path("src/nativeTest/resources/headers")).toString()
        listOf(
            ClangTarget.MacosArm64,
            ClangTarget.MacosX64,
            ClangTarget.MingwX64,
            ClangTarget.LinuxX64,
            ClangTarget.IosDeviceArm64,
            ClangTarget.IosSimulatorArm64,
            ClangTarget.IosSimulatorX64,
        ).forEach { target ->
            val types = mutableMapOf<CxNumber, Int>()
            useIndex { index ->
                useTranslationUnit(
                    index = index,
                    headers = setOf("primitives.h"),
                    compilerArgs = clangCompilerArguments(target, TestenvOptions) + listOf("-I$headersPath")
                ) { translationUnit ->
                    translationUnit.cursor.visitChildren { cursor ->
                        if (cursor.spelling != "primitives") return@visitChildren

                        cursor.ensureKind(CXCursor_StructDecl)
                        cursor.visitChildren { fieldCursor ->
                            fieldCursor.ensureKind(CXCursor_FieldDecl)

                            val type = when (val typeKind = fieldCursor.type.kind) {
                                CXType_Char_U,
                                CXType_Char_S     -> CxNumber.Char

                                CXType_SChar      -> CxNumber.SignedChar
                                CXType_UChar      -> CxNumber.UnsignedChar
                                CXType_Short      -> CxNumber.Short
                                CXType_UShort     -> CxNumber.UnsignedShort
                                CXType_Int        -> CxNumber.Int
                                CXType_UInt       -> CxNumber.UnsignedInt
                                CXType_Long       -> CxNumber.Long
                                CXType_ULong      -> CxNumber.UnsignedLong
                                CXType_LongLong   -> CxNumber.LongLong
                                CXType_ULongLong  -> CxNumber.UnsignedLongLong
                                CXType_Int128     -> CxNumber.Int128
                                CXType_UInt128    -> CxNumber.UnsignedInt128
                                CXType_Float      -> CxNumber.Float
                                CXType_Double     -> CxNumber.Double
                                CXType_LongDouble -> CxNumber.LongDouble
                                else              -> error("unexpected type: $typeKind")
                            }
                            val typeSize = clang_Type_getSizeOf(fieldCursor.type).toInt()
                            check(types.put(type, typeSize) == null) { "$type already registered" }
                        }
                    }
                }
            }
            val targetTypes = when (target) {
                ClangTarget.IosDeviceArm64    -> CxNumbers.IosDeviceArm64
                ClangTarget.IosSimulatorArm64 -> CxNumbers.IosSimulatorArm64
                ClangTarget.IosSimulatorX64   -> CxNumbers.IosSimulatorX64
                ClangTarget.MacosArm64        -> CxNumbers.MacosArm64
                ClangTarget.MacosX64          -> CxNumbers.MacosX64
                ClangTarget.LinuxX64          -> CxNumbers.LinuxX64
                ClangTarget.MingwX64          -> CxNumbers.MingwX64
            }
            assertEquals(types, targetTypes.values, target.toString())
        }
    }
}
