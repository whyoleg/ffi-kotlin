package dev.whyoleg.foreign.tool.clang.compiler

public enum class ClangDependencyKind {
    LLVM,
    MINGW_TOOLCHAIN,
    LINUX_GCC_TOOLCHAIN,
    MACOS_SDK,
    IOS_DEVICE_SDK,
    IOS_SIMULATOR_SDK
}

// TODO: make some of them nullable?
// TODO: decide on API here - may be make it similar to what we have in gradle plugin with map
public data class ClangDependencies(
    val llvmPath: String?,
    val mingwToolchainPath: String?,
    val linuxGccToolchainPath: String?,
    val macosSdkPath: String?,
    val iosDeviceSdkPath: String?,
    val iosSimulatorSdkPath: String?,
)

public fun ClangArguments.forTarget(
    target: ClangTarget,
    dependencies: ClangDependencies
): List<String> {
    requireNotNull(dependencies.llvmPath) { "LLVM path is required" }

    val isystemPath = "${dependencies.llvmPath}/lib/clang/16.0.0/include"
    val prefixPath = "${dependencies.llvmPath}/usr/bin"
    return when (target) {
        ClangTarget.MingwX64          -> mingwX64(
            isystemPath,
            prefixPath,
            requireNotNull(dependencies.mingwToolchainPath) { "mingwToolchainPath is required" }
        )

        ClangTarget.LinuxX64          -> linuxX64(
            isystemPath,
            prefixPath,
            requireNotNull(dependencies.linuxGccToolchainPath) { "linuxGccToolchainPath is required" }
        )

        ClangTarget.MacosX64          -> macosX64(
            isystemPath,
            prefixPath,
            requireNotNull(dependencies.macosSdkPath) { "macosSdkPath is required" }
        )

        ClangTarget.MacosArm64        -> macosArm64(
            isystemPath,
            prefixPath,
            requireNotNull(dependencies.macosSdkPath) { "macosSdkPath is required" }
        )

        ClangTarget.IosDeviceArm64    -> iosDeviceArm64(
            isystemPath,
            prefixPath,
            requireNotNull(dependencies.iosDeviceSdkPath) { "iosDeviceSdkPath is required" }
        )

        ClangTarget.IosSimulatorArm64 -> iosSimulatorArm64(
            isystemPath,
            prefixPath,
            requireNotNull(dependencies.iosSimulatorSdkPath) { "iosSimulatorSdkPath is required" }
        )

        ClangTarget.IosSimulatorX64   -> iosSimulatorX64(
            isystemPath,
            prefixPath,
            requireNotNull(dependencies.iosSimulatorSdkPath) { "iosSimulatorSdkPath is required" }
        )
    }
}
