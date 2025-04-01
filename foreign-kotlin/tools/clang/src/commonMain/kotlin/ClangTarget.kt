package dev.whyoleg.foreign.tool.clang

// TODO: support all K targets
public sealed interface ClangTarget {
    public sealed interface Desktop : ClangTarget
    public sealed interface Apple : ClangTarget
    public sealed interface Macos : Apple, Desktop
    public sealed interface Ios : Apple


    public data object LinuxX64 : Desktop
    public data object MingwX64 : Desktop

    public data object MacosArm64 : Macos
    public data object MacosX64 : Macos

    public data object IosDeviceArm64 : Ios
    public data object IosSimulatorArm64 : Ios
    public data object IosSimulatorX64 : Ios
}
