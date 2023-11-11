package dev.whyoleg.foreign.gradle.dsl

import dev.whyoleg.foreign.gradle.dsl.cxinterop.*
import dev.whyoleg.foreign.gradle.tooling.*
import org.gradle.api.file.*
import org.gradle.api.provider.*

public interface ForeignExtension {
    public val cxInterops: ForeignCxInteropContainer
    public fun cxInterops(configure: ForeignCxInteropContainer.() -> Unit): Unit = configure(cxInterops)

    public val toolchainDependencies: MapProperty<ToolchainDependency, FileSystemLocation>
}
