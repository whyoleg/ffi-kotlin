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

        val headers = listOf(
            "bio.h",
            "evp.h",
            "bn.h",
            "aes.h",
            "types.h",
        )

        repeat(100) {
            listOf(
                CxTarget.MacosArm64 to "macos-arm64",
                CxTarget.MacosX64 to "macos-x64",
                CxTarget.MingwX64 to "mingw-x64",
                CxTarget.LinuxX64 to "linux-x64",
                CxTarget.IosDeviceArm64 to "ios-device-arm64",
                CxTarget.IosSimulatorArm64 to "ios-simulator-arm64",
                CxTarget.IosSimulatorX64 to "ios-simulator-x64",
            ).forEach { (target, dirName) ->
                headers.forEach { header ->
                    val (index, time) = measureTimedValue {
                        CxCompiler.buildIndex(
                            mainFilePath = "$baseIncludePath/$dirName/include/openssl/$header",
                            compilerArgs = CxCompilerArguments.forTarget(target, dependencies) + listOf(
                                "-I$baseIncludePath/$dirName/include"
                            )
                        )
                    }
                    println("$target/$header: $time")
                    assertTrue(
                        index.enums.isNotEmpty() ||
                                index.records.isNotEmpty() ||
                                index.functions.isNotEmpty() ||
                                index.typedefs.isNotEmpty()
                    )
                }
            }
        }
    }
}
