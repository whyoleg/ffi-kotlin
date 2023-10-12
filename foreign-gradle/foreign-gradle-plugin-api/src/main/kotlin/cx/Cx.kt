package dev.whyoleg.foreign.gradle.api.cx

public enum class CxTarget {
    MingwX64,
    LinuxX64,
    MacosX64,
    MacosArm64,
    IosDeviceArm64,
    IosSimulatorArm64,
    IosSimulatorX64,
}

public enum class CxDependency {
    LLVM,
    MingwToolchain,
    LinuxGccToolchain,
    MacosSdk,
    IosDeviceSdk,
    IosSimulatorSdk,
}
