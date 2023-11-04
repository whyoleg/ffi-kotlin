package dev.whyoleg.foreign.gradle

import dev.whyoleg.foreign.gradle.dsl.*
import dev.whyoleg.foreign.gradle.internal.*
import org.gradle.api.*

public class ForeignPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val project = ProjectPrototype.wrap(target)
        val extension = DefaultForeignExtension(project)
        target.extensions.add(ForeignExtension::class.java, "foreign", extension)
    }
}
