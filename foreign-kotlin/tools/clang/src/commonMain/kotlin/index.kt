package dev.whyoleg.foreign.tool.clang

import dev.whyoleg.foreign.tool.clang.api.*

// TODO: support all K targets
// TODO: take values from konan.properties
public enum class ClangTarget(
    internal val llvmTarget: String
) {
    LinuxX64("x86_64-unknown-linux-gnu"),
    LinuxArm64("aarch64-unknown-linux-gnu"),
    MingwX64("x86_64-pc-windows-gnu"),
    MacosArm64("arm64-apple-macos10.16"),
    MacosX64("x86_64-apple-macos10.13"),
    IosDeviceArm64("arm64-apple-ios9.0"),
    IosSimulatorArm64("arm64-apple-ios9.0-simulator"),
    IosSimulatorX64("x86_64-apple-ios9.0-simulator"),
}

public enum class ClangOption(
    internal val default: String? = null,
) {
    LLVM_PATH,
    LLVM_VERSION("16.0.0"),
    MINGW_X64_TOOLCHAIN_PATH,
    LINUX_X64_TOOLCHAIN_PATH,
    LINUX_ARM64_TOOLCHAIN_PATH,
    MACOS_SDK_PATH,
    IOS_DEVICE_SDK_PATH,
    IOS_SIMULATOR_SDK_PATH
}

public fun index(
    target: ClangTarget,
    options: Map<ClangOption, String>,
    headers: Set<String>,
    compilerArgs: List<String>
): CxIndex = ClangCompiler.buildIndex(
    headers = headers,
    compilerArgs = clangCompilerArguments(target, options) + compilerArgs
)
