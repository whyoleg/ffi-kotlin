package dev.whyoleg.foreign.gradle.internal.tool

import dev.whyoleg.foreign.gradle.api.cx.*
import dev.whyoleg.foreign.tooling.cx.compiler.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import dev.whyoleg.foreign.tooling.cx.compiler.runner.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.*

public object ForeignGradleTool

@Suppress("UnusedReceiverParameter")
@OptIn(ExperimentalSerializationApi::class)
public fun ForeignGradleTool.buildIndex(
    inputFile: File,
    outputFile: File,
    target: CxTarget,
    dependencies: Map<CxDependency, File>,
    additionalCompilerArguments: List<String>
) {
    val index = CxCompiler.buildIndex(
        mainFileName = inputFile.name, //TODO?
        mainFilePath = inputFile.absolutePath,
        compilerArgs = CxCompilerArguments.forTarget(
            target = when (target) {
                CxTarget.MingwX64          -> CxCompilerTarget.MingwX64
                CxTarget.LinuxX64          -> CxCompilerTarget.LinuxX64
                CxTarget.MacosX64          -> CxCompilerTarget.MacosX64
                CxTarget.MacosArm64        -> CxCompilerTarget.MacosArm64
                CxTarget.IosDeviceArm64    -> CxCompilerTarget.IosDeviceArm64
                CxTarget.IosSimulatorArm64 -> CxCompilerTarget.IosSimulatorArm64
                CxTarget.IosSimulatorX64   -> CxCompilerTarget.IosSimulatorX64
            },
            dependencies = CxCompilerDependencies(
                llvmPath = dependencies[CxDependency.LLVM]?.absolutePath,
                mingwToolchainPath = dependencies[CxDependency.MingwToolchain]?.absolutePath,
                linuxGccToolchainPath = dependencies[CxDependency.LinuxGccToolchain]?.absolutePath,
                macosSdkPath = dependencies[CxDependency.MacosSdk]?.absolutePath,
                iosDeviceSdkPath = dependencies[CxDependency.IosDeviceSdk]?.absolutePath,
                iosSimulatorSdkPath = dependencies[CxDependency.IosSimulatorSdk]?.absolutePath,
            )
        ) + additionalCompilerArguments
    )
    outputFile.outputStream().use {
        Json.encodeToStream(CxCompilerIndex.serializer(), index, it)
    }
}
