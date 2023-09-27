package dev.whyoleg.foreign.tooling.cx.compiler.test.support

import dev.whyoleg.foreign.tooling.cx.compiler.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import dev.whyoleg.foreign.tooling.cx.compiler.runner.*

// TODO: fill those values via build config plugin
private val compilerDependencies = CxCompilerDependencies(
    llvmPath = "/Users/whyoleg/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials",
    mingwToolchainPath = "/Users/whyoleg/.konan/dependencies/msys2-mingw-w64-x86_64-2",
    linuxGccToolchainPath = "/Users/whyoleg/.konan/dependencies/x86_64-unknown-linux-gnu-gcc-8.3.0-glibc-2.19-kernel-4.9-2",
    macosSdkPath = "/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX.sdk",
    iosDeviceSdkPath = "/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS.sdk",
    iosSimulatorSdkPath = "/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator.sdk",
)

private val openssl3RootPath: String =
    "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/test-projects/libcrypto/api/build/tmp/setupOpenssl3"

private fun openssl3TargetDirName(target: CxCompilerTarget): String = when (target) {
    CxCompilerTarget.MacosArm64        -> "macos-arm64"
    CxCompilerTarget.MacosX64          -> "macos-x64"
    CxCompilerTarget.MingwX64          -> "mingw-x64"
    CxCompilerTarget.LinuxX64          -> "linux-x64"
    CxCompilerTarget.IosDeviceArm64    -> "ios-device-arm64"
    CxCompilerTarget.IosSimulatorArm64 -> "ios-simulator-arm64"
    CxCompilerTarget.IosSimulatorX64   -> "ios-simulator-x64"
    else                               -> TODO()
}

public fun compilerArgs(target: CxCompilerTarget): List<String> = CxCompilerArguments.forTarget(target, compilerDependencies)

public fun openssl3IncludeDir(target: CxCompilerTarget): String = "$openssl3RootPath/${openssl3TargetDirName(target)}/include"
public fun openssl3IncludeDir(target: CxCompilerTarget, headerPath: String): String =
    "$openssl3RootPath/${openssl3TargetDirName(target)}/include/$headerPath"

public fun CxCompiler.buildIndexOverOpenssl3(
    header: String,
    target: CxCompilerTarget
): CxCompilerIndex = buildIndex(
    mainFileName = header,
    mainFilePath = openssl3IncludeDir(target, header),
    compilerArgs = compilerArgs(target) + listOf("-I${openssl3IncludeDir(target)}")
)
