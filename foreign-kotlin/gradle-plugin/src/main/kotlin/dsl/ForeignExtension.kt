package dev.whyoleg.foreign.gradle.plugin.dsl

import dev.whyoleg.foreign.gradle.plugin.dsl.cinterface.*
import org.gradle.api.*

public interface ForeignExtension {
    public val cInterfaces: NamedDomainObjectContainer<out ForeignCInterface>
    public fun cInterface(name: String, configure: ForeignCInterface.() -> Unit)

    //public val toolchainDependencies: MapProperty<ToolchainDependency, FileSystemLocation>
}
