package dev.whyoleg.foreign.gradle.plugin.internal.cinterfaces

import dev.whyoleg.foreign.gradle.plugin.dsl.*
import dev.whyoleg.foreign.gradle.plugin.dsl.cinterface.*
import dev.whyoleg.foreign.gradle.plugin.internal.*
import org.gradle.api.*
import org.gradle.api.model.*
import org.gradle.api.provider.*

internal class DefaultJvmCInterface(
    private val objects: ObjectFactory
) : DefaultPlatformCInterface(objects), ForeignJvmCInterface {
    override val foreignPlatform: ForeignPlatform get() = ForeignPlatform.Jvm

    override val runtimeKind: Property<ForeignJvmRuntimeKind> =
        objects.property(ForeignJvmRuntimeKind::class.java).convention(ForeignJvmRuntimeKind.BOTH)

    override val targets: NamedDomainObjectContainer<DefaultJvmTargetCInterface> =
        objects.domainObjectContainer(DefaultJvmTargetCInterface::class.java)

    override fun macosArm64(configure: ForeignJvmTargetCInterface.() -> Unit) {
        target(ForeignTarget.MacosArm64).configure()
    }

    override fun macosX64(configure: ForeignJvmTargetCInterface.() -> Unit) {
        target(ForeignTarget.MacosX64).configure()
    }

    override fun linuxX64(configure: ForeignJvmTargetCInterface.() -> Unit) {
        target(ForeignTarget.LinuxX64).configure()
    }

    override fun mingwX64(configure: ForeignJvmTargetCInterface.() -> Unit) {
        target(ForeignTarget.MingwX64).configure()
    }

    private fun target(target: ForeignTarget.Jvm): ForeignJvmTargetCInterface {
        return targets.getOrCreate(target.name) {
            DefaultJvmTargetCInterface(objects, target).withAllFrom(this)
        }
    }
}

internal class DefaultJvmTargetCInterface(
    objects: ObjectFactory,
    override val foreignTarget: ForeignTarget.Jvm
) : DefaultTargetCInterface(objects), ForeignJvmTargetCInterface
