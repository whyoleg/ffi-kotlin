package dev.whyoleg.foreign.gradle.internal.tool

import dev.whyoleg.foreign.gradle.api.*
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
    target: ForeignTarget,
    dependencies: Map<ForeignDependency, File>,
    additionalCompilerArguments: List<String>
) {
    val index = CxCompiler.buildIndex(
        mainFileName = inputFile.name, //TODO?
        mainFilePath = inputFile.absolutePath,
        compilerArgs = CxCompilerArguments.forTarget(
            target = when (target) {
                ForeignTarget.MingwX64          -> CxCompilerTarget.MingwX64
                ForeignTarget.LinuxX64          -> CxCompilerTarget.LinuxX64
                ForeignTarget.MacosX64          -> CxCompilerTarget.MacosX64
                ForeignTarget.MacosArm64        -> CxCompilerTarget.MacosArm64
                ForeignTarget.IosDeviceArm64    -> CxCompilerTarget.IosDeviceArm64
                ForeignTarget.IosSimulatorArm64 -> CxCompilerTarget.IosSimulatorArm64
                ForeignTarget.IosSimulatorX64   -> CxCompilerTarget.IosSimulatorX64
            },
            dependencies = CxCompilerDependencies(
                llvmPath = dependencies[ForeignDependency.LLVM]?.absolutePath,
                mingwToolchainPath = dependencies[ForeignDependency.MingwToolchain]?.absolutePath,
                linuxGccToolchainPath = dependencies[ForeignDependency.LinuxGccToolchain]?.absolutePath,
                macosSdkPath = dependencies[ForeignDependency.MacosSdk]?.absolutePath,
                iosDeviceSdkPath = dependencies[ForeignDependency.IosDeviceSdk]?.absolutePath,
                iosSimulatorSdkPath = dependencies[ForeignDependency.IosSimulatorSdk]?.absolutePath,
            )
        ) + additionalCompilerArguments
    )
    outputFile.outputStream().use {
        Json.encodeToStream(CxCompilerIndex.serializer(), index, it)
    }
}
