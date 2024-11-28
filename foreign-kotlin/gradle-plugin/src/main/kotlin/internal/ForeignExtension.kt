package dev.whyoleg.foreign.gradle.internal

import dev.whyoleg.foreign.gradle.dsl.*
import dev.whyoleg.foreign.gradle.dsl.c.*
import org.gradle.api.*
import org.gradle.api.model.*

internal class DefaultForeignExtension(
    private val objects: ObjectFactory,
) : ForeignExtension {
    override val interfaces: PolymorphicDomainObjectContainer<out ForeignInterface> =
        objects.polymorphicDomainObjectContainer(ForeignInterface::class.java).apply {
            // register
        }

    override fun c(name: String, configure: ForeignCInterface.() -> Unit) {
        TODO("Not yet implemented")
    }
}
