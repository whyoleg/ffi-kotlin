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
    target: CxTarget,
    dependencies: CxCompilerDependencies
): List<String> {
    val isystemPath = "${dependencies.llvmPath}/lib/clang/11.1.0/include"
    val prefixPath = "${dependencies.llvmPath}/usr/bin"
    return when (target) {
        CxTarget.MingwX64          -> mingwX64(isystemPath, prefixPath, dependencies.mingwToolchainPath)
        CxTarget.LinuxX64          -> linuxX64(isystemPath, prefixPath, dependencies.linuxGccToolchainPath)
        CxTarget.MacosX64          -> macosX64(isystemPath, prefixPath, dependencies.macosSdkPath)
        CxTarget.MacosArm64        -> macosArm64(isystemPath, prefixPath, dependencies.macosSdkPath)
        CxTarget.IosDeviceArm64    -> iosDeviceArm64(isystemPath, prefixPath, dependencies.iosDeviceSdkPath)
        CxTarget.IosSimulatorArm64 -> iosSimulatorArm64(isystemPath, prefixPath, dependencies.iosSimulatorSdkPath)
        CxTarget.IosSimulatorX64   -> iosSimulatorX64(isystemPath, prefixPath, dependencies.iosSimulatorSdkPath)
    }
}
