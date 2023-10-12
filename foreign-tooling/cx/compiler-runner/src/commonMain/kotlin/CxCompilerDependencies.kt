package dev.whyoleg.foreign.tooling.cx.compiler.runner

import dev.whyoleg.foreign.tooling.cx.compiler.model.*

// TODO: make some of them nullable?
// TODO: decide on API here - may be make it similar to what we have in gradle plugin with map
public data class CxCompilerDependencies(
    val llvmPath: String?,
    val mingwToolchainPath: String?,
    val linuxGccToolchainPath: String?,
    val macosSdkPath: String?,
    val iosDeviceSdkPath: String?,
    val iosSimulatorSdkPath: String?,
)

public fun CxCompilerArguments.forTarget(
    target: CxCompilerTarget,
    dependencies: CxCompilerDependencies
): List<String> {
    requireNotNull(dependencies.llvmPath) { "LLVM path is required" }

    val isystemPath = "${dependencies.llvmPath}/lib/clang/11.1.0/include"
    val prefixPath = "${dependencies.llvmPath}/usr/bin"
    return when (target) {
        CxCompilerTarget.MingwX64          -> mingwX64(
            isystemPath,
            prefixPath,
            requireNotNull(dependencies.mingwToolchainPath) { "mingwToolchainPath is required" })
        CxCompilerTarget.LinuxX64          -> linuxX64(
            isystemPath,
            prefixPath,
            requireNotNull(dependencies.linuxGccToolchainPath) { "linuxGccToolchainPath is required" })
        CxCompilerTarget.MacosX64          -> macosX64(
            isystemPath,
            prefixPath,
            requireNotNull(dependencies.macosSdkPath) { "macosSdkPath is required" })
        CxCompilerTarget.MacosArm64        -> macosArm64(
            isystemPath,
            prefixPath,
            requireNotNull(dependencies.macosSdkPath) { "macosSdkPath is required" })
        CxCompilerTarget.IosDeviceArm64    -> iosDeviceArm64(
            isystemPath,
            prefixPath,
            requireNotNull(dependencies.iosDeviceSdkPath) { "iosDeviceSdkPath is required" })
        CxCompilerTarget.IosSimulatorArm64 -> iosSimulatorArm64(
            isystemPath,
            prefixPath,
            requireNotNull(dependencies.iosSimulatorSdkPath) { "iosSimulatorSdkPath is required" })
        CxCompilerTarget.IosSimulatorX64   -> iosSimulatorX64(
            isystemPath,
            prefixPath,
            requireNotNull(dependencies.iosSimulatorSdkPath) { "iosSimulatorSdkPath is required" })
    }
}
