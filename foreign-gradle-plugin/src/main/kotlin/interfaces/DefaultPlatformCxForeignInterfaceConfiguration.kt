package dev.whyoleg.foreign.gradle.interfaces

import dev.whyoleg.foreign.gradle.api.interfaces.*
import org.gradle.api.*
import org.gradle.api.model.*

internal sealed class DefaultPlatformCxForeignInterfaceConfiguration(
    val platformName: String,
    objectFactory: ObjectFactory,
    parent: DefaultRootCxForeignInterfaceConfiguration
) : DefaultLeafCxForeignInterfaceConfiguration(objectFactory, parent),
    PlatformCxForeignInterfaceConfiguration,
    Named {
    final override fun getName(): String = platformName
    internal val interfaceName = parent.interfaceName
}
