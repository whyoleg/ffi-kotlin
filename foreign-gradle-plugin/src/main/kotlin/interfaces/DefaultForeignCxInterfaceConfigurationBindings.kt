package dev.whyoleg.foreign.gradle.interfaces

import dev.whyoleg.foreign.gradle.*
import dev.whyoleg.foreign.gradle.api.interfaces.*
import org.gradle.api.*
import org.gradle.api.model.*
import org.gradle.api.provider.*
import org.gradle.api.specs.*
import org.gradle.kotlin.dsl.*

internal class DefaultLeafForeignCxInterfaceConfigurationBindings(
    objectFactory: ObjectFactory,
    parent: DefaultForeignCxInterfaceConfigurationBindings?
) : DefaultForeignCxInterfaceConfigurationBindings(objectFactory, parent)

internal class DefaultRootForeignCxInterfaceConfigurationBindings(
    defaultPackageName: String,
    objectFactory: ObjectFactory,
) : DefaultForeignCxInterfaceConfigurationBindings(objectFactory, null),
    RootCxForeignInterfaceConfiguration.Bindings {

    override val public: Property<Boolean> = objectFactory.property<Boolean>().convention(false)
    override val requiresOptIn: Property<String?> = objectFactory.property<String?>().convention(null as String?)

    private val packageName: Property<Transformer<String, String>> = objectFactory.property<Transformer<String, String>>()
        .convention { defaultPackageName }

    override fun packageName(transformer: Transformer<String, String>) {
        packageName.set(transformer)
    }
}

internal abstract class DefaultForeignCxInterfaceConfigurationBindings(
    objectFactory: ObjectFactory,
    parent: DefaultForeignCxInterfaceConfigurationBindings?
) : BaseCxForeignInterfaceConfiguration.Bindings {
    final override val initialHeaders: ListProperty<String> =
        objectFactory.listProperty<String>().withAllFrom(parent?.initialHeaders)

    private val includeHeaders: ListProperty<Spec<String>> =
        objectFactory.listProperty<Spec<String>>().withAllFrom(parent?.includeHeaders)

    private val excludeHeaders: ListProperty<Spec<String>> =
        objectFactory.listProperty<Spec<String>>().withAllFrom(parent?.excludeHeaders)

    final override fun includeHeaders(spec: Spec<String>) {
        includeHeaders.add(spec)
    }

    final override fun excludeHeaders(spec: Spec<String>) {
        excludeHeaders.add(spec)
    }
}
