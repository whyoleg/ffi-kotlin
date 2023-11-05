package dev.whyoleg.foreign.gradle.internal.cx

import dev.whyoleg.foreign.gradle.dsl.cxinterop.*
import dev.whyoleg.foreign.gradle.internal.*
import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.provider.*
import org.gradle.api.specs.*

internal abstract class DefaultBaseCxInterop(project: ProjectPrototype) : BaseCxInterop {
    override val includeDirectories: ListProperty<Directory> = project.objects.listProperty(Directory::class.java)
    override val librarySearchDirectories: ListProperty<Directory> = project.objects.listProperty(Directory::class.java)
    override val libraryLinkageNames: ListProperty<String> = project.objects.listProperty(String::class.java)
    override val initialHeaders: ListProperty<String> = project.objects.listProperty(String::class.java)
    override val includeHeaders: ListProperty<Spec<String>> =
        project.objects.listProperty(Spec::class.java as Class<Spec<String>>)
    override val excludeHeaders: ListProperty<Spec<String>> =
        project.objects.listProperty(Spec::class.java as Class<Spec<String>>)
    override val packageName: Property<Transformer<String, String>> =
        project.objects.property(Transformer::class.java as Class<Transformer<String, String>>)
}

internal abstract class DefaultPlatformCxInterop(project: ProjectPrototype) : DefaultBaseCxInterop(project),
    PlatformCxInterop, Named {
    final override fun getName(): String = kotlinPlatform.name
}

internal abstract class DefaultTargetCxInterop(project: ProjectPrototype) : DefaultBaseCxInterop(project),
    TargetCxInterop, Named {
    final override fun getName(): String = nativeTarget.name
}

internal fun DefaultBaseCxInterop.withAllFrom(other: DefaultBaseCxInterop) {
    includeDirectories.addAll(other.includeDirectories)
    librarySearchDirectories.addAll(other.librarySearchDirectories)
    libraryLinkageNames.addAll(other.libraryLinkageNames)
    initialHeaders.addAll(other.initialHeaders)
    includeHeaders.addAll(other.includeHeaders)
    excludeHeaders.addAll(other.excludeHeaders)
    packageName.convention(other.packageName)
}
