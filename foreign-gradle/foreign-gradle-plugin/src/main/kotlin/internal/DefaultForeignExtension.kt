package dev.whyoleg.foreign.gradle.internal

import dev.whyoleg.foreign.gradle.dsl.*
import dev.whyoleg.foreign.gradle.dsl.cxinterop.*
import dev.whyoleg.foreign.gradle.internal.cx.*
import dev.whyoleg.foreign.gradle.tooling.*
import org.gradle.api.file.*
import org.gradle.api.provider.*

internal class DefaultForeignExtension(
    private val project: ProjectPrototype
) : ForeignExtension {
    override val cxInterops: ForeignCxInteropContainer = DefaultForeignCxInteropContainer(
        project.objects.domainObjectContainer(ForeignCxInterop::class.java, DefaultForeignCxInterop.Factory(project))
    )
    override val toolchainDependencies: MapProperty<ToolchainDependency, FileSystemLocation> =
        project.objects.mapProperty(ToolchainDependency::class.java, FileSystemLocation::class.java)
}
