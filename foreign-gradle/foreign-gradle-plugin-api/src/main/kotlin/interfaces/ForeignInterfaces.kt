package dev.whyoleg.foreign.gradle.api.interfaces

import org.gradle.api.*

public interface ForeignInterfaces : PolymorphicDomainObjectContainer<ForeignInterfaceConfiguration> {
    public fun cx(
        name: String,
        configure: RootCxForeignInterfaceConfiguration.() -> Unit = {}
    ): RootCxForeignInterfaceConfiguration = maybeCreate(name, RootCxForeignInterfaceConfiguration::class.java).apply(configure)
}
