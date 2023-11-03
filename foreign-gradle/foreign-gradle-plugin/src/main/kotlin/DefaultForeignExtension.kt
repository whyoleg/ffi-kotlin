package dev.whyoleg.foreign.gradle

import dev.whyoleg.foreign.gradle.api.*
import dev.whyoleg.foreign.gradle.interfaces.*
import org.gradle.api.*
import org.gradle.api.model.*

internal class DefaultForeignExtension(
    objectFactory: ObjectFactory
) : ForeignExtension {
    override val interfaces: ForeignInterfaceContainer = DefaultForeignInterfaces(objectFactory)
}

internal class ProjectPrototype(
    private val project: Project,
) {
    val tasks get() = project.tasks
}
