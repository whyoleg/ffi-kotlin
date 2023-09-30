package dev.whyoleg.foreign.gradle.interfaces

import dev.whyoleg.foreign.gradle.interfaces.cx.*
import org.gradle.api.*
import org.gradle.kotlin.dsl.*

public interface ForeignInterfaceConfiguration : Named

public class ForeignInterfaces internal constructor(
    delegate: PolymorphicDomainObjectContainer<ForeignInterfaceConfiguration>
) : PolymorphicDomainObjectContainer<ForeignInterfaceConfiguration> by delegate {
    public fun cx(
        name: String,
        configure: RootForeignCxInterfaceConfiguration.() -> Unit
    ): NamedDomainObjectProvider<RootForeignCxInterfaceConfiguration> {
        // TODO: register vs (configure or create)
        return register<RootForeignCxInterfaceConfiguration>(name, configure)
    }
}
