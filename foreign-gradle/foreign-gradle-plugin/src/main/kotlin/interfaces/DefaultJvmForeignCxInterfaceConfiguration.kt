package dev.whyoleg.foreign.gradle.interfaces

import dev.whyoleg.foreign.gradle.api.*
import dev.whyoleg.foreign.gradle.api.cx.*
import dev.whyoleg.foreign.gradle.api.cx.JvmPlatformCxForeignInterfaceConfiguration.*
import org.gradle.api.*
import org.gradle.api.model.*
import org.gradle.api.provider.*
import org.gradle.kotlin.dsl.*

internal class DefaultJvmPlatformCxForeignInterfaceConfiguration(
    platformName: String,
    objectFactory: ObjectFactory,
    parent: DefaultCxForeignInterface
) : DefaultPlatformCxForeignInterfaceConfiguration(platformName, objectFactory, parent),
    JvmPlatformCxForeignInterfaceConfiguration {

    override val runtimeKind: Property<RuntimeKind> = objectFactory.property<RuntimeKind>().convention(RuntimeKind.JNI)

    val hosts = objectFactory.domainObjectContainer(
        DefaultJvmTargetCxForeignInterfaceConfiguration::class,
        DefaultJvmTargetCxForeignInterfaceConfiguration.Factory(objectFactory, this)
    )

    override fun macosArm64(configure: JvmTargetCxForeignInterfaceConfiguration.() -> Unit) {
        hosts.maybeCreate("macosArm64").apply(configure)
    }

    override fun macosX64(configure: JvmTargetCxForeignInterfaceConfiguration.() -> Unit) {
        hosts.maybeCreate("macosX64").apply(configure)
    }

    override fun linuxX64(configure: JvmTargetCxForeignInterfaceConfiguration.() -> Unit) {
        hosts.maybeCreate("linuxX64").apply(configure)
    }

    override fun mingwX64(configure: JvmTargetCxForeignInterfaceConfiguration.() -> Unit) {
        hosts.maybeCreate("mingwX64").apply(configure)
    }

    class Factory(
        private val objectFactory: ObjectFactory,
        private val parent: DefaultCxForeignInterface
    ) : NamedDomainObjectFactory<DefaultJvmPlatformCxForeignInterfaceConfiguration> {
        override fun create(name: String): DefaultJvmPlatformCxForeignInterfaceConfiguration =
            DefaultJvmPlatformCxForeignInterfaceConfiguration(name, objectFactory, parent)
    }
}

internal class DefaultJvmTargetCxForeignInterfaceConfiguration(
    name: String,
    objectFactory: ObjectFactory,
    parent: DefaultJvmPlatformCxForeignInterfaceConfiguration
) : DefaultTargetCxForeignInterfaceConfiguration(name, objectFactory, parent),
    JvmTargetCxForeignInterfaceConfiguration {
    override val target: ForeignTarget
        get() = when (name) {
            "macosArm64" -> ForeignTarget.MacosArm64
            else         -> TODO("unsupported target: $name")
        }

    class Factory(
        private val objectFactory: ObjectFactory,
        private val parent: DefaultJvmPlatformCxForeignInterfaceConfiguration
    ) : NamedDomainObjectFactory<DefaultJvmTargetCxForeignInterfaceConfiguration> {
        override fun create(name: String): DefaultJvmTargetCxForeignInterfaceConfiguration {
            return DefaultJvmTargetCxForeignInterfaceConfiguration(name, objectFactory, parent)
        }
    }
}
