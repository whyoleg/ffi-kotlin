package dev.whyoleg.foreign.gradle.api.cx

public interface AndroidCxForeignInterfaceConfiguration : BaseCxForeignInterfaceConfiguration

public interface AndroidPlatformCxForeignInterfaceConfiguration :
    AndroidCxForeignInterfaceConfiguration,
    PlatformCxForeignInterfaceConfiguration {
    // TODO: decide on naming and add others
    public fun arm64(configure: AndroidTargetCxForeignInterfaceConfiguration.() -> Unit = {})
    public fun x64(configure: AndroidTargetCxForeignInterfaceConfiguration.() -> Unit = {})
}

public interface AndroidTargetCxForeignInterfaceConfiguration :
    AndroidCxForeignInterfaceConfiguration,
    TargetCxForeignInterfaceConfiguration {

}
