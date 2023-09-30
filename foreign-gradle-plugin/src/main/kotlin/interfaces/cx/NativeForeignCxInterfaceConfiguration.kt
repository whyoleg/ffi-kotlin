package dev.whyoleg.foreign.gradle.interfaces.cx

public sealed interface NativeForeignCxInterfaceConfiguration : ForeignCxInterfaceConfiguration

public sealed interface NativeRootForeignCxInterfaceConfiguration : NativeForeignCxInterfaceConfiguration {
    public fun macosArm64(configure: NativeTargetForeignCxInterfaceConfiguration.() -> Unit = {})
    public fun macosX64(configure: NativeTargetForeignCxInterfaceConfiguration.() -> Unit = {})
    public fun linuxX64(configure: NativeTargetForeignCxInterfaceConfiguration.() -> Unit = {})
    public fun mingwX64(configure: NativeTargetForeignCxInterfaceConfiguration.() -> Unit = {})
    // TODO: add ios
}

public sealed interface NativeTargetForeignCxInterfaceConfiguration : NativeForeignCxInterfaceConfiguration {
    //embedStaticLibraries
    //embedLinkPaths
}
