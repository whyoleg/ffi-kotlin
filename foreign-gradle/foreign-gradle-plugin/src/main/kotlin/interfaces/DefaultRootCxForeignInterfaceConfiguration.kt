package dev.whyoleg.foreign.gradle.interfaces

import dev.whyoleg.foreign.gradle.api.interfaces.*
import org.gradle.api.model.*
import org.gradle.api.provider.*
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.plugin.*
import javax.inject.*

internal abstract class DefaultRootCxForeignInterfaceConfiguration @Inject constructor(
    val interfaceName: String,
    objectFactory: ObjectFactory
) : DefaultBaseCxForeignInterfaceConfiguration(objectFactory, null),
    RootCxForeignInterfaceConfiguration {
    override fun getName(): String = interfaceName

    override val sourceSetTree: Property<KotlinSourceSetTree> =
        objectFactory
            .property<KotlinSourceSetTree>()
            .convention(KotlinSourceSetTree.main)

    override val bindings: DefaultRootForeignCxInterfaceConfigurationBindings =
        DefaultRootForeignCxInterfaceConfigurationBindings("TODO", objectFactory)

    val platforms = objectFactory.polymorphicDomainObjectContainer(
        DefaultPlatformCxForeignInterfaceConfiguration::class
    ).apply {
        registerFactory(
            DefaultJvmPlatformCxForeignInterfaceConfiguration::class.java,
            DefaultJvmPlatformCxForeignInterfaceConfiguration.Factory(objectFactory, this@DefaultRootCxForeignInterfaceConfiguration)
        )
    }

    override fun jvm(configure: JvmPlatformCxForeignInterfaceConfiguration.() -> Unit) {
        platforms.maybeCreate("jvm", DefaultJvmPlatformCxForeignInterfaceConfiguration::class).apply(configure)
    }

    override fun native(configure: NativePlatformCxForeignInterfaceConfiguration.() -> Unit) {
//        platforms.maybeCreate("native", DefaultJvmPlatformForeignCxInterfaceConfiguration::class).apply(configure)
    }

    override fun android(configure: AndroidPlatformCxForeignInterfaceConfiguration.() -> Unit) {
//        platforms.maybeCreate("android", DefaultJvmPlatformForeignCxInterfaceConfiguration::class).apply(configure)
    }
}

