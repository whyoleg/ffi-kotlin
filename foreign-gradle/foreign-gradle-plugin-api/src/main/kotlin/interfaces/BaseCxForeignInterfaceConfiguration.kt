package dev.whyoleg.foreign.gradle.api.interfaces

import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.provider.*
import org.gradle.api.specs.*

// there are:
//   - root/global/per-module configuration
//   - platform configuration
//   - target configuration
public interface BaseCxForeignInterfaceConfiguration {
    public val includeDirectories: ListProperty<Directory>

    // TODO: may be libraryDirectories should be accessible only for final targets
    public val libraryDirectories: ListProperty<Directory>
    public val libraryLinkageNames: ListProperty<String>

    public val bindings: Bindings

    public interface Bindings {
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
    }

    public operator fun <T : Bindings> T.invoke(configure: T.() -> Unit): Unit = configure(this)
    public operator fun <T : Bindings> T.invoke(action: Action<T>): Unit = action.execute(this)
}

public interface PlatformCxForeignInterfaceConfiguration : BaseCxForeignInterfaceConfiguration

// TODO: decide on name better?
public interface TargetCxForeignInterfaceConfiguration : BaseCxForeignInterfaceConfiguration {
    // TODO: link to index generation task
}
