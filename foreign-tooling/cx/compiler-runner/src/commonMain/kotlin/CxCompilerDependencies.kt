package dev.whyoleg.foreign.tooling.cx.compiler.runner

import dev.whyoleg.foreign.tooling.cx.compiler.model.*

// TODO: make some of them nullable?
public data class CxCompilerDependencies(
    val llvmPath: String,
    val mingwToolchainPath: String,
    val linuxGccToolchainPath: String,
    val macosSdkPath: String,
    val iosDeviceSdkPath: String,
    val iosSimulatorSdkPath: String,
)

public fun CxCompilerArguments.forTarget(
    target: CxCompilerTarget,
    dependencies: CxCompilerDependencies
): List<String> {
    val isystemPath = "${dependencies.llvmPath}/lib/clang/11.1.0/include"
    val prefixPath = "${dependencies.llvmPath}/usr/bin"
    return when (target) {
        CxCompilerTarget.MingwX64          -> mingwX64(isystemPath, prefixPath, dependencies.mingwToolchainPath)
        CxCompilerTarget.LinuxX64          -> linuxX64(isystemPath, prefixPath, dependencies.linuxGccToolchainPath)
        CxCompilerTarget.MacosX64          -> macosX64(isystemPath, prefixPath, dependencies.macosSdkPath)
        CxCompilerTarget.MacosArm64        -> macosArm64(isystemPath, prefixPath, dependencies.macosSdkPath)
        CxCompilerTarget.IosDeviceArm64    -> iosDeviceArm64(isystemPath, prefixPath, dependencies.iosDeviceSdkPath)
        CxCompilerTarget.IosSimulatorArm64 -> iosSimulatorArm64(isystemPath, prefixPath, dependencies.iosSimulatorSdkPath)
        CxCompilerTarget.IosSimulatorX64   -> iosSimulatorX64(isystemPath, prefixPath, dependencies.iosSimulatorSdkPath)
    }
}
