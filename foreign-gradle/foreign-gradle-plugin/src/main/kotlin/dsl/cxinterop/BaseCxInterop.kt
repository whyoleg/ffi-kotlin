package dev.whyoleg.foreign.gradle.dsl.cxinterop

import dev.whyoleg.foreign.gradle.tooling.*
import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.provider.*
import org.gradle.api.specs.*

public interface BaseCxInterop {
    public val includeDirectories: ListProperty<Directory>

    // TODO: may be libraryDirectories should be accessible only for TargetCxInterop?
    public val librarySearchDirectories: ListProperty<Directory>
    public val libraryLinkageNames: ListProperty<String>

    public val initialHeaders: ListProperty<String>

    // these are 'soft' filters
    // means that referenced declarations will be still included
    // f.e if we only include `openssl/*` headers
    // and in some declaration (X), there will be reference to some declaration (Y) from `stdio.h` header
    // this declaration (Y) will be also included (not referenced declarations will not be included)
    // otherwise there will be code, which could not be compiled
    // can be called multiple times
    public val includeHeaders: ListProperty<Spec<String>>
    public val excludeHeaders: ListProperty<Spec<String>>

    public fun includeHeaders(spec: Spec<String>): Unit = includeHeaders.add(spec)
    public fun excludeHeaders(spec: Spec<String>): Unit = excludeHeaders.add(spec)

    // by default will be {project.group}.{project.name.replace("-", ".")}.{interface.name}
    // for now, all of this is by header - could be improved later
    // header -> package name
    // single value - will override previously set value
    public val packageName: Property<Transformer<String, String>>
    public fun packageName(transformer: Transformer<String, String>): Unit = packageName.set(transformer)
    public fun packageName(value: String): Unit = packageName { value }
}

public interface PlatformCxInterop : BaseCxInterop {
    public val kotlinPlatform: KotlinPlatform
}

public interface TargetCxInterop : BaseCxInterop {
    public val nativeTarget: NativeTarget
    // TODO: link to index generation task
}
