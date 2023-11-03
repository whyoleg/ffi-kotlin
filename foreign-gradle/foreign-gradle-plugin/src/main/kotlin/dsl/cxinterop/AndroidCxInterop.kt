package dev.whyoleg.foreign.gradle.dsl.cxinterop

public interface AndroidBaseCxInterop : BaseCxInterop

public interface AndroidPlatformCxInterop : AndroidBaseCxInterop, PlatformCxInterop {
    // TODO: decide on naming and add others
    public fun arm64(configure: AndroidTargetCxInterop.() -> Unit = {})
    public fun x64(configure: AndroidTargetCxInterop.() -> Unit = {})
}

public interface AndroidTargetCxInterop : AndroidBaseCxInterop, TargetCxInterop {

}
