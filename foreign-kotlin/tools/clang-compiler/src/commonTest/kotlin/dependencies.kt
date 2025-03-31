package dev.whyoleg.foreign.tool.clang.compiler

import kotlinx.io.*
import kotlinx.io.files.*
import kotlin.random.*

// TODO: fill those values via build config plugin
private val compilerDependencies = ClangDependencies(
    llvmPath = "/Users/Oleg.Yukhnevich/.konan/dependencies/llvm-16.0.0-aarch64-macos-essentials-63",
    mingwToolchainPath = "/Users/Oleg.Yukhnevich/.konan/dependencies/msys2-mingw-w64-x86_64-2",
    linuxGccToolchainPath = "/Users/Oleg.Yukhnevich/.konan/dependencies/x86_64-unknown-linux-gnu-gcc-8.3.0-glibc-2.19-kernel-4.9-2",
    macosSdkPath = "/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX.sdk",
    iosDeviceSdkPath = "/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS.sdk",
    iosSimulatorSdkPath = "/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator.sdk",
)

private val openssl3RootPath: String = TestConstants.OPENSSL3_ROOT_PATH

private fun openssl3TargetDirName(target: ClangTarget): String = when (target) {
    ClangTarget.MacosArm64        -> "macos-arm64"
    ClangTarget.MacosX64          -> "macos-x64"
    ClangTarget.MingwX64          -> "mingw-x64"
    ClangTarget.LinuxX64          -> "linux-x64"
    ClangTarget.IosDeviceArm64    -> "ios-device-arm64"
    ClangTarget.IosSimulatorArm64 -> "ios-simulator-arm64"
    ClangTarget.IosSimulatorX64   -> "ios-simulator-x64"
}

fun compilerArgs(target: ClangTarget): List<String> =
    ClangArguments.forTarget(target, compilerDependencies)

fun openssl3IncludeDir(target: ClangTarget): String =
    "$openssl3RootPath/${openssl3TargetDirName(target)}/include"

fun openssl3CompilerArgs(target: ClangTarget): List<String> =
    compilerArgs(target) + listOf("-I${openssl3IncludeDir(target)}")

fun createHeadersFile(headers: Set<String>): String {
    val headersContent = buildString {
        headers.forEach { header ->
            appendLine("#include <$header>")
        }
    }
    val headersPath = Path(SystemTemporaryDirectory, "headers-${Random.nextInt()}.h")
    SystemFileSystem.sink(headersPath).buffered().use { output ->
        output.writeString(headersContent)
    }
    return headersPath.toString()
}
