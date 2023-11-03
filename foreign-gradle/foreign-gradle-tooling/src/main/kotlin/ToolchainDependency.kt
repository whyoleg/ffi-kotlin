package dev.whyoleg.foreign.gradle.tooling

public enum class ToolchainDependency {
    LLVM,
    MingwToolchain,
    LinuxGccToolchain,
    MacosSdk,
    IosDeviceSdk,
    IosSimulatorSdk,
}
