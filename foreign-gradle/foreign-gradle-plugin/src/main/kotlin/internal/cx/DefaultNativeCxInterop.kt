package dev.whyoleg.foreign.gradle.internal.cx

import dev.whyoleg.foreign.gradle.dsl.cxinterop.*
import dev.whyoleg.foreign.gradle.internal.*
import dev.whyoleg.foreign.gradle.tooling.*
import org.gradle.api.provider.*

internal class DefaultNativePlatformCxInterop(
    private val project: ProjectPrototype
) : DefaultPlatformCxInterop(project),
    NativePlatformCxInterop {

    override val kotlinPlatform: KotlinPlatform get() = KotlinPlatform.Native

    val targets = project.objects.domainObjectContainer(DefaultNativeTargetCxInterop::class.java)

    private fun target(
        target: NativeTarget
    ): NativeTargetCxInterop = targets.findByName(target.name) ?: DefaultNativeTargetCxInterop(project, target).also { obj ->
        obj.withAllFrom(this)
    }.also(targets::add)

    override fun macosArm64(configure: NativeTargetCxInterop.() -> Unit) {
        target(NativeTarget.MacosArm64).configure()
    }

    override fun macosX64(configure: NativeTargetCxInterop.() -> Unit) {
        target(NativeTarget.MacosX64).configure()
    }

    override fun linuxX64(configure: NativeTargetCxInterop.() -> Unit) {
        target(NativeTarget.LinuxX64).configure()
    }

    override fun mingwX64(configure: NativeTargetCxInterop.() -> Unit) {
        target(NativeTarget.MingwX64).configure()
    }
}

internal class DefaultNativeTargetCxInterop(
    project: ProjectPrototype,
    override val nativeTarget: NativeTarget
) : DefaultTargetCxInterop(project),
    NativeTargetCxInterop {
}
