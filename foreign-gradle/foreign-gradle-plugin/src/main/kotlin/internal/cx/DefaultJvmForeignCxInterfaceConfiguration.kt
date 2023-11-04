package dev.whyoleg.foreign.gradle.internal.cx

import dev.whyoleg.foreign.gradle.dsl.cxinterop.*
import dev.whyoleg.foreign.gradle.internal.*
import dev.whyoleg.foreign.gradle.tooling.*
import org.gradle.api.provider.*

internal class DefaultJvmPlatformCxInterop(
    project: ProjectPrototype
) : DefaultPlatformCxInterop(project),
    JvmPlatformCxInterop {

    override val kotlinPlatform: KotlinPlatform get() = KotlinPlatform.Jvm

    override val runtimeKind: Property<JvmRuntimeKind> =
        project.objects.property(JvmRuntimeKind::class.java).convention(JvmRuntimeKind.JNI)

    val targets =
        project.objects.domainObjectContainer(DefaultJvmTargetCxInterop::class.java)

    override fun macosArm64(configure: JvmTargetCxInterop.() -> Unit) {
//        hosts.maybeCreate("macosArm64").apply(configure)
    }

    override fun macosX64(configure: JvmTargetCxInterop.() -> Unit) {
//        hosts.maybeCreate("macosX64").apply(configure)
    }

    override fun linuxX64(configure: JvmTargetCxInterop.() -> Unit) {
//        hosts.maybeCreate("linuxX64").apply(configure)
    }

    override fun mingwX64(configure: JvmTargetCxInterop.() -> Unit) {
//        hosts.maybeCreate("mingwX64").apply(configure)
    }
}

internal class DefaultJvmTargetCxInterop(
    project: ProjectPrototype,
    override val nativeTarget: NativeTarget
) : DefaultTargetCxInterop(project),
    JvmTargetCxInterop {
}
