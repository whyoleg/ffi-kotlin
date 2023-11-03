package dev.whyoleg.foreign.gradle.dsl.cxinterop

public interface NativeCxInterop : BaseCxInterop

public interface NativePlatformCxInterop : NativeCxInterop, PlatformCxInterop {

    public fun macosArm64(configure: NativeTargetCxInterop.() -> Unit = {})
    public fun macosX64(configure: NativeTargetCxInterop.() -> Unit = {})
    public fun linuxX64(configure: NativeTargetCxInterop.() -> Unit = {})
    public fun mingwX64(configure: NativeTargetCxInterop.() -> Unit = {})
    // TODO: add ios
}

public interface NativeTargetCxInterop : NativeCxInterop, TargetCxInterop {
    //embedStaticLibraries
    //embedLinkPaths
}
