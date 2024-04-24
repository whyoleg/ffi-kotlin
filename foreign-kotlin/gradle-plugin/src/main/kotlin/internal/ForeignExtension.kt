package dev.whyoleg.foreign.gradle.plugin.internal

import dev.whyoleg.foreign.gradle.plugin.dsl.*
import dev.whyoleg.foreign.gradle.plugin.dsl.cinterface.*
import dev.whyoleg.foreign.gradle.plugin.internal.cinterfaces.*
import org.gradle.api.*
import org.gradle.api.model.*

internal class DefaultForeignExtension(
    private val objects: ObjectFactory,
) : ForeignExtension {
    override val cInterfaces: NamedDomainObjectContainer<DefaultCInterface> =
        objects.domainObjectContainer(DefaultCInterface::class.java)

    override fun cInterface(name: String, configure: ForeignCInterface.() -> Unit) {
        cInterfaces.getOrCreate(name) {
            DefaultCInterface(objects, name)
        }.configure()
    }
}
