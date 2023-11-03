package dev.whyoleg.foreign.gradle.api

public interface ForeignExtension {
    public val interfaces: ForeignInterfaceContainer
    public fun interfaces(configure: ForeignInterfaceContainer.() -> Unit): Unit = configure(interfaces)
}
