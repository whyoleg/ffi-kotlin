package dev.whyoleg.foreign.gradle.internal

import org.gradle.api.*
import org.gradle.api.model.*
import org.gradle.api.tasks.*

internal class ProjectPrototype private constructor(
    val tasks: TaskContainer,
    val objects: ObjectFactory
) {
    constructor(project: Project) : this(
        project.tasks,
        project.objects
    )
}
