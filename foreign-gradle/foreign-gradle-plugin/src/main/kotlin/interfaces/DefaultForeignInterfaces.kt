package dev.whyoleg.foreign.gradle.interfaces

import dev.whyoleg.foreign.gradle.api.*
import dev.whyoleg.foreign.gradle.api.cx.*
import org.gradle.api.*
import org.gradle.api.model.*
import org.gradle.kotlin.dsl.*

internal class DefaultForeignInterfaces private constructor(
    delegate: PolymorphicDomainObjectContainer<ForeignInterface>
) : ForeignInterfaceContainer,
    PolymorphicDomainObjectContainer<ForeignInterface> by delegate {
    constructor(objectFactory: ObjectFactory) : this(
        objectFactory.polymorphicDomainObjectContainer(ForeignInterface::class).apply {
            registerBinding(CxForeignInterface::class.java, DefaultCxForeignInterface::class.java)
        }
    )

    override fun cx(name: String, configure: CxForeignInterface.() -> Unit): CxForeignInterface {

    }
}
