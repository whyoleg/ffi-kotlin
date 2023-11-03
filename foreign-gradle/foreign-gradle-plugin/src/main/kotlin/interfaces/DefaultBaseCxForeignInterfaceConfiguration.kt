package dev.whyoleg.foreign.gradle.interfaces

import dev.whyoleg.foreign.gradle.*
import dev.whyoleg.foreign.gradle.api.cx.*
import dev.whyoleg.foreign.gradle.api.interfaces.*
import org.gradle.api.file.*
import org.gradle.api.model.*
import org.gradle.api.provider.*
import org.gradle.kotlin.dsl.*

internal abstract class DefaultLeafCxForeignInterfaceConfiguration(
    objectFactory: ObjectFactory,
    parent: DefaultBaseCxForeignInterfaceConfiguration?
) : DefaultBaseCxForeignInterfaceConfiguration(objectFactory, parent) {
    final override val bindings: DefaultForeignCxInterfaceConfigurationBindings =
        DefaultLeafForeignCxInterfaceConfigurationBindings(objectFactory, parent?.bindings)
}

internal abstract class DefaultBaseCxForeignInterfaceConfiguration(
    objectFactory: ObjectFactory,
    parent: DefaultBaseCxForeignInterfaceConfiguration?
) : BaseCxForeignInterfaceConfiguration {
    final override val includeDirectories: ListProperty<Directory> =
        objectFactory.listProperty<Directory>().withAllFrom(parent?.includeDirectories)
    final override val libraryDirectories: ListProperty<Directory> =
        objectFactory.listProperty<Directory>().withAllFrom(parent?.libraryDirectories)
    final override val libraryLinkageNames: ListProperty<String> =
        objectFactory.listProperty<String>().withAllFrom(parent?.libraryLinkageNames)
    abstract override val bindings: DefaultForeignCxInterfaceConfigurationBindings
}
