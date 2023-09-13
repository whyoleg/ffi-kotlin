package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.internal.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import dev.whyoleg.foreign.tooling.cx.compiler.runner.*
import kotlin.test.*

class PrimitivesTest {

    @Test
    fun test() {
        val primitivesFilePath =
            "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/foreign-tooling/cx/compiler/src/nativeTest/resources/primitives.h"

        val dependencies = CxCompilerDependencies(
            llvmPath = "/Users/whyoleg/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials",
            mingwToolchainPath = "/Users/whyoleg/.konan/dependencies/msys2-mingw-w64-x86_64-2",
            linuxGccToolchainPath = "/Users/whyoleg/.konan/dependencies/x86_64-unknown-linux-gnu-gcc-8.3.0-glibc-2.19-kernel-4.9-2",
            macosSdkPath = "/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX.sdk",
            iosDeviceSdkPath = "/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS.sdk",
            iosSimulatorSdkPath = "/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator.sdk",
        )

        listOf(
            CxTarget.MacosArm64,
            CxTarget.MacosX64,
            CxTarget.MingwX64,
            CxTarget.LinuxX64,
            CxTarget.IosDeviceArm64,
            CxTarget.IosSimulatorArm64,
            CxTarget.IosSimulatorX64,
        ).forEach { target ->
            val compilerArgs = CxCompilerArguments.forTarget(target, dependencies)
            val result = useIndex { index ->
                useTranslationUnit(index, primitivesFilePath, compilerArgs) { translationUnit ->
                    useIndexAction(index) { action ->
                        indexTranslationUnit(action, translationUnit, CxIndexPrimitiveCollector())
                    }
                }
            }
            assertEquals(result, target.builtinTypeSizes, target.toString())
        }
    }
}
