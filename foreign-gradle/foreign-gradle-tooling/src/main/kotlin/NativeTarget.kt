package dev.whyoleg.foreign.gradle.tooling

// TODO: decide on better naming to not confuse with K/N target
//  may be use ForeignTarget...
public enum class NativeTarget {
    MingwX64,
    LinuxX64,
    MacosX64,
    MacosArm64,
    IosDeviceArm64,
    IosSimulatorArm64,
    IosSimulatorX64,
    Wasm32
}
