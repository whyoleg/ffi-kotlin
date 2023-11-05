package dev.whyoleg.foreign.gradle.internal.cx

import dev.whyoleg.foreign.gradle.dsl.cxinterop.*
import dev.whyoleg.foreign.gradle.internal.*
import dev.whyoleg.foreign.gradle.tooling.*
import org.gradle.api.*
import org.gradle.api.provider.*
import org.jetbrains.kotlin.gradle.plugin.*

internal class DefaultForeignCxInterop(
    private val project: ProjectPrototype,
    private val name: String,
) : DefaultBaseCxInterop(project),
    ForeignCxInterop {
    override fun getName(): String = name
    override val sourceSetTree: Property<KotlinSourceSetTree> =
        project.objects.property(KotlinSourceSetTree::class.java).convention(KotlinSourceSetTree.main)
    override val publicApi: Property<Boolean> =
        project.objects.property(Boolean::class.java).convention(false)
    override val requiresOptIn: Property<String?> =
        project.objects.property(String::class.java).convention(null as String?)

    val platforms = project.objects.domainObjectContainer(DefaultPlatformCxInterop::class.java)

    private inline fun <reified T : DefaultPlatformCxInterop> platform(
        platform: KotlinPlatform,
        create: (project: ProjectPrototype) -> T
    ): T = platforms.findByName(platform.name) as? T ?: create(project).also {
        it.withAllFrom(this)
    }.also(platforms::add)

    override fun jvm(configure: JvmPlatformCxInterop.() -> Unit) {
        platform(KotlinPlatform.Jvm, ::DefaultJvmPlatformCxInterop).configure()
    }

    override fun native(configure: NativePlatformCxInterop.() -> Unit) {
//        platforms.maybeCreate("native", DefaultJvmPlatformForeignCxInterfaceConfiguration::class).apply(configure)
    }

    override fun android(configure: AndroidPlatformCxInterop.() -> Unit) {
//        platforms.maybeCreate("android", DefaultJvmPlatformForeignCxInterfaceConfiguration::class).apply(configure)
    }

    class Factory(private val project: ProjectPrototype) : NamedDomainObjectFactory<ForeignCxInterop> {
        override fun create(name: String): ForeignCxInterop = DefaultForeignCxInterop(project, name)
    }
}

