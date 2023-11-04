package dev.whyoleg.foreign.gradle.internal

import dev.whyoleg.foreign.gradle.dsl.*
import dev.whyoleg.foreign.gradle.dsl.cxinterop.*
import dev.whyoleg.foreign.gradle.internal.cx.*

internal class DefaultForeignExtension(
    private val project: ProjectPrototype
) : ForeignExtension {
    override val cxInterops: ForeignCxInteropContainer = DefaultForeignCxInteropContainer(
        project.objects.domainObjectContainer(ForeignCxInterop::class.java)
    )
}
