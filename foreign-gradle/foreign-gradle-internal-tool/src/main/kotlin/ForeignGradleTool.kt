package dev.whyoleg.foreign.gradle.internal.tool

import dev.whyoleg.foreign.gradle.tooling.*
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
    target: NativeTarget,
    dependencies: Map<ToolchainDependency, File>,
    additionalCompilerArguments: List<String>
) {
    val index = CxCompiler.buildIndex(
        mainFileName = inputFile.name, //TODO?
        mainFilePath = inputFile.absolutePath,
        compilerArgs = CxCompilerArguments.forTarget(
            target = when (target) {
                NativeTarget.MingwX64 -> CxCompilerTarget.MingwX64
                NativeTarget.LinuxX64 -> CxCompilerTarget.LinuxX64
                NativeTarget.MacosX64 -> CxCompilerTarget.MacosX64
                NativeTarget.MacosArm64 -> CxCompilerTarget.MacosArm64
                NativeTarget.IosDeviceArm64 -> CxCompilerTarget.IosDeviceArm64
                NativeTarget.IosSimulatorArm64 -> CxCompilerTarget.IosSimulatorArm64
                NativeTarget.IosSimulatorX64 -> CxCompilerTarget.IosSimulatorX64
                NativeTarget.Wasm32 -> TODO()
            },
            dependencies = CxCompilerDependencies(
                llvmPath = dependencies[ToolchainDependency.LLVM]?.absolutePath,
                mingwToolchainPath = dependencies[ToolchainDependency.MingwToolchain]?.absolutePath,
                linuxGccToolchainPath = dependencies[ToolchainDependency.LinuxGccToolchain]?.absolutePath,
                macosSdkPath = dependencies[ToolchainDependency.MacosSdk]?.absolutePath,
                iosDeviceSdkPath = dependencies[ToolchainDependency.IosDeviceSdk]?.absolutePath,
                iosSimulatorSdkPath = dependencies[ToolchainDependency.IosSimulatorSdk]?.absolutePath,
            )
        ) + additionalCompilerArguments
    )
    outputFile.outputStream().use {
        Json.encodeToStream(CxCompilerIndex.serializer(), index, it)
    }
}
