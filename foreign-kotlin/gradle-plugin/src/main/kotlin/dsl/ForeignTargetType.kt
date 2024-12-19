package dev.whyoleg.foreign.gradle.dsl

import org.gradle.api.*
import java.io.*

public enum class ForeignTargetType : Named, Serializable {
    MacosArm64,
    MacosX64,
    LinuxArm64,
    LinuxX64,
    MingwX64,

    IosDeviceArm64,
    IosSimulatorArm64,
    IosSimulatorX64,

    AndroidArm32,
    AndroidArm64,
    AndroidX64,
    AndroidX86,

    Wasm;

    override fun toString(): String = name
    override fun getName(): String = name
}
