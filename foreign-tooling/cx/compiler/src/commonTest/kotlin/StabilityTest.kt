package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import dev.whyoleg.foreign.tooling.cx.compiler.runner.*
import kotlin.test.*
import kotlin.time.*

class StabilityTest {
    @Test
    fun test() {
        val dependencies = CxCompilerDependencies(
            llvmPath = "/Users/whyoleg/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials",
            mingwToolchainPath = "/Users/whyoleg/.konan/dependencies/msys2-mingw-w64-x86_64-2",
            linuxGccToolchainPath = "/Users/whyoleg/.konan/dependencies/x86_64-unknown-linux-gnu-gcc-8.3.0-glibc-2.19-kernel-4.9-2",
            macosSdkPath = "/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX.sdk",
            iosDeviceSdkPath = "/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS.sdk",
            iosSimulatorSdkPath = "/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator.sdk",
        )

        val baseIncludePath = "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/test-projects/libcrypto/api/build/tmp/setupOpenssl3"

        val runs = buildList {
            repeat(100) {
                listOf(
                    CxCompilerTarget.MacosArm64,
                    CxCompilerTarget.MacosX64,
                    CxCompilerTarget.MingwX64,
                    CxCompilerTarget.LinuxX64,
                    CxCompilerTarget.IosDeviceArm64,
                    CxCompilerTarget.IosSimulatorArm64,
                    CxCompilerTarget.IosSimulatorX64,
                ).forEach { target ->
                    listOf(
                        "ec.h",
                        "bio.h",
                        "evp.h",
                        "bn.h",
                        "aes.h",
                        "types.h",
                        "encoder.h",
                    ).forEach { header ->
                        add(target to header)
                    }
                }
            }
        }

        runs.shuffled().forEachIndexed { index, (target, header) ->
            val dirName = when (target) {
                CxCompilerTarget.MacosArm64        -> "macos-arm64"
                CxCompilerTarget.MacosX64          -> "macos-x64"
                CxCompilerTarget.MingwX64          -> "mingw-x64"
                CxCompilerTarget.LinuxX64          -> "linux-x64"
                CxCompilerTarget.IosDeviceArm64    -> "ios-device-arm64"
                CxCompilerTarget.IosSimulatorArm64 -> "ios-simulator-arm64"
                CxCompilerTarget.IosSimulatorX64   -> "ios-simulator-x64"
            }

            val (result, time) = measureTimedValue {
                CxCompiler.buildIndex(
                    mainFilePath = "$baseIncludePath/$dirName/include/openssl/$header",
                    compilerArgs = CxCompilerArguments.forTarget(target, dependencies) + listOf(
                        "-I$baseIncludePath/$dirName/include"
                    )
                )
            }
            val r = runs.size.toString()
            val i = index.toString().padStart(r.length)
            println("[$i/$r] $target/$header: $time")
            assertTrue(
                result.enums.isNotEmpty() ||
                        result.records.isNotEmpty() ||
                        result.functions.isNotEmpty() ||
                        result.typedefs.isNotEmpty()
            )
        }
    }
}
