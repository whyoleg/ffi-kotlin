package dev.whyoleg.foreign.gradle.dsl

public enum class ForeignTarget {
    MacosArm64,
    MacosX64,
    LinuxX64,
    MingwX64,

    IosDeviceArm64,
    IosSimulatorArm64,
    IosSimulatorX64,

    AndroidArm32,
    AndroidArm64,
    AndroidX64,
    AndroidX86,

    Wasm,
}
