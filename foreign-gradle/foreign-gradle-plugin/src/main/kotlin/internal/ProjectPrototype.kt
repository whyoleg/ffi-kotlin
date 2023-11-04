package dev.whyoleg.foreign.gradle.internal

import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.model.*
import org.gradle.api.tasks.*

internal class ProjectPrototype private constructor(
    val tasks: TaskContainer,
    val objects: ObjectFactory,
    val layout: ProjectLayout
) {
    companion object {
        fun wrap(project: Project): ProjectPrototype {
            return ProjectPrototype(
                project.tasks,
                project.objects,
                project.layout
            )
        }
    }
}
