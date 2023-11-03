package dev.whyoleg.foreign.gradle.api.cx

public interface NativeCxForeignInterfaceConfiguration : BaseCxForeignInterfaceConfiguration

public interface NativePlatformCxForeignInterfaceConfiguration :
    NativeCxForeignInterfaceConfiguration,
    PlatformCxForeignInterfaceConfiguration {

    public fun macosArm64(configure: NativeTargetCxForeignInterfaceConfiguration.() -> Unit = {})
    public fun macosX64(configure: NativeTargetCxForeignInterfaceConfiguration.() -> Unit = {})
    public fun linuxX64(configure: NativeTargetCxForeignInterfaceConfiguration.() -> Unit = {})
    public fun mingwX64(configure: NativeTargetCxForeignInterfaceConfiguration.() -> Unit = {})
    // TODO: add ios
}

public interface NativeTargetCxForeignInterfaceConfiguration :
    NativeCxForeignInterfaceConfiguration,
    TargetCxForeignInterfaceConfiguration {
    //embedStaticLibraries
    //embedLinkPaths
}
