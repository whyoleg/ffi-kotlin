package dev.whyoleg.foreign.tool.clang.testenv

import dev.whyoleg.foreign.tool.clang.*
import dev.whyoleg.foreign.tool.clang.ClangOption.*
import dev.whyoleg.foreign.tool.clang.api.*

public val TestenvOptions: Map<ClangOption, String> = mapOf(
    LLVM_PATH to TestenvConstants.LLVM_PATH,
    MINGW_X64_TOOLCHAIN_PATH to TestenvConstants.MINGW_X64_TOOLCHAIN_PATH,
    LINUX_X64_TOOLCHAIN_PATH to TestenvConstants.LINUX_X64_TOOLCHAIN_PATH,
    LINUX_ARM64_TOOLCHAIN_PATH to TestenvConstants.LINUX_ARM64_TOOLCHAIN_PATH,
    // TODO: fill those values from the system (check K/N)
    MACOS_SDK_PATH to "/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX.sdk",
    IOS_DEVICE_SDK_PATH to "/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS.sdk",
    IOS_SIMULATOR_SDK_PATH to "/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator.sdk",
)

public fun indexWithOpenssl3Headers(
    target: ClangTarget,
    headers: Set<String>,
    compilerArgs: List<String> = emptyList()
): CxIndex {
    val openssl3TargetDirName = when (target) {
        ClangTarget.MacosArm64        -> "macos-arm64"
        ClangTarget.MacosX64          -> "macos-x64"
        ClangTarget.MingwX64          -> "mingw-x64"
        ClangTarget.LinuxX64          -> "linux-x64"
        ClangTarget.LinuxArm64        -> "linux-arm64"
        ClangTarget.IosDeviceArm64    -> "ios-device-arm64"
        ClangTarget.IosSimulatorArm64 -> "ios-simulator-arm64"
        ClangTarget.IosSimulatorX64   -> "ios-simulator-x64"
    }
    return index(
        target = target,
        options = TestenvOptions,
        headers = headers,
        compilerArgs = compilerArgs + listOf(
            "-I${TestenvConstants.OPENSSL3_ROOT_PATH}/${openssl3TargetDirName}/include"
        )
    )
}
