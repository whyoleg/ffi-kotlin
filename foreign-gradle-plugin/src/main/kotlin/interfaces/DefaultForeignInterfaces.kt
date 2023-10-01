package dev.whyoleg.foreign.gradle.interfaces

import dev.whyoleg.foreign.gradle.api.interfaces.*
import org.gradle.api.*
import org.gradle.api.model.*
import org.gradle.kotlin.dsl.*

internal class DefaultForeignInterfaces private constructor(
    delegate: PolymorphicDomainObjectContainer<ForeignInterfaceConfiguration>
) : ForeignInterfaces,
    PolymorphicDomainObjectContainer<ForeignInterfaceConfiguration> by delegate {
    constructor(objectFactory: ObjectFactory) : this(
        objectFactory.polymorphicDomainObjectContainer(ForeignInterfaceConfiguration::class).apply {
            registerBinding(RootCxForeignInterfaceConfiguration::class.java, DefaultRootCxForeignInterfaceConfiguration::class.java)
        }
    )
}
