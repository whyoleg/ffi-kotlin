package dev.whyoleg.foreign.gradle

import dev.whyoleg.foreign.gradle.interfaces.*
import org.gradle.api.model.*
import org.gradle.kotlin.dsl.*
import javax.inject.*

public abstract class ForeignExtension @Inject constructor(
    private val objectFactory: ObjectFactory
) {
    public val interfaces: ForeignInterfaces = ForeignInterfaces(
        objectFactory.polymorphicDomainObjectContainer(ForeignInterfaceConfiguration::class)
    )

    public fun interfaces(configure: ForeignInterfaces.() -> Unit): Unit = configure(interfaces)
}
