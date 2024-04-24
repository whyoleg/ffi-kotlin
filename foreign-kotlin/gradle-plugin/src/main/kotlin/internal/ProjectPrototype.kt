package dev.whyoleg.foreign.gradle.plugin.internal

import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.model.*
import org.gradle.api.tasks.*

internal fun ProjectPrototype(project: Project): ProjectPrototype = ProjectPrototype(
    project.tasks,
    project.objects,
    project.layout
)

internal class ProjectPrototype(
    val tasks: TaskContainer,
    val objects: ObjectFactory,
    val layout: ProjectLayout
)
