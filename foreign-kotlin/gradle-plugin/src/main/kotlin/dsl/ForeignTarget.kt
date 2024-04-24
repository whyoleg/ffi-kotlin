package dev.whyoleg.foreign.gradle.plugin.dsl

// TODO: add os, family, arch, etc
public sealed interface ForeignTarget {
    public sealed interface Jvm : ForeignTarget
    public sealed interface Android : ForeignTarget
    public sealed interface Native : ForeignTarget
    public sealed interface Js : ForeignTarget
    public sealed interface WasmJs : ForeignTarget

    public object MacosArm64 : Native, Jvm
    public object MacosX64 : Native, Jvm
    public object LinuxX64 : Native, Jvm
    public object MingwX64 : Native, Jvm

    public object IosDeviceArm64 : Native
    public object IosSimulatorArm64 : Native
    public object IosSimulatorX64 : Native

    public object AndroidArm32 : Native, Android
    public object AndroidArm64 : Native, Android
    public object AndroidX64 : Native, Android
    public object AndroidX86 : Native, Android

    public object Wasm : Js, WasmJs

    public val name: String get() = this.javaClass.simpleName // TODO: recheck
}

public enum class ForeignPlatform {
    Jvm,
    Android,
    Native,
    Js,
    WasmJs
}
