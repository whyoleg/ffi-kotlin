package dev.whyoleg.foreign.gradle.interfaces

import dev.whyoleg.foreign.gradle.api.cx.*
import org.gradle.api.model.*
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.plugin.*
import javax.inject.*

internal abstract class DefaultCxForeignInterface @Inject constructor(
    val interfaceName: String,
    objectFactory: ObjectFactory
) : DefaultBaseCxForeignInterfaceConfiguration(objectFactory, null),
    CxForeignInterface {
    override fun getName(): String = interfaceName

    abstract override val bindings: DefaultRootForeignCxInterfaceConfigurationBindings

    val platforms = objectFactory.polymorphicDomainObjectContainer(
        DefaultPlatformCxForeignInterfaceConfiguration::class
    ).apply {
        registerFactory(
            DefaultJvmPlatformCxForeignInterfaceConfiguration::class.java,
            DefaultJvmPlatformCxForeignInterfaceConfiguration.Factory(
                objectFactory,
                this@DefaultCxForeignInterface
            )
        )
    }

    init {
        run {
            sourceSetTree.convention(KotlinSourceSetTree.main)
            bindings.packageName.convention { "TODO" }
        }
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

