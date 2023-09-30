package dev.whyoleg.foreign.gradle.interfaces.cx

public sealed interface AndroidForeignCxInterfaceConfiguration : ForeignCxInterfaceConfiguration

public sealed interface AndroidRootForeignCxInterfaceConfiguration : AndroidForeignCxInterfaceConfiguration {
    // TODO: decide on naming and add others
    public fun arm64(configure: AndroidAbiForeignCxInterfaceConfiguration.() -> Unit = {})
    public fun x64(configure: AndroidAbiForeignCxInterfaceConfiguration.() -> Unit = {})
}

public sealed interface AndroidAbiForeignCxInterfaceConfiguration : AndroidForeignCxInterfaceConfiguration {

}
