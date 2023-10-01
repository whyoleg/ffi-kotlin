package dev.whyoleg.foreign.gradle.api.interfaces

import org.gradle.api.*
import org.gradle.kotlin.dsl.*

public interface ForeignInterfaces : PolymorphicDomainObjectContainer<ForeignInterfaceConfiguration> {
    public fun cx(
        name: String,
        configure: RootCxForeignInterfaceConfiguration.() -> Unit = {}
    ): RootCxForeignInterfaceConfiguration = maybeCreate<RootCxForeignInterfaceConfiguration>(name).apply(configure)
}
