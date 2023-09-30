package dev.whyoleg.foreign.gradle.interfaces.cx

import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.provider.*
import org.gradle.api.specs.*

public sealed interface ForeignCxInterfaceConfiguration {
    public val includeDirectories: ListProperty<Directory>

    // TODO: may be libraryDirectories should be accessible only for final targets
    public val libraryDirectories: ListProperty<Directory>
    public val libraryLinkageNames: ListProperty<String>

    public val bindings: Bindings

    public sealed interface Bindings {
        public val initialHeaders: ListProperty<String>
        public fun includeHeaders(spec: Spec<String>)
        public fun excludeHeaders(spec: Spec<String>)
    }

    public operator fun <T : Bindings> T.invoke(configure: T.() -> Unit): Unit = configure(this)
    public operator fun <T : Bindings> T.invoke(action: Action<T>): Unit = action.execute(this)
}
