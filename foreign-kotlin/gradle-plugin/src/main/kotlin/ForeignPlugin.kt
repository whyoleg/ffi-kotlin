package dev.whyoleg.foreign.gradle

import dev.whyoleg.foreign.gradle.dsl.*
import dev.whyoleg.foreign.gradle.internal.*
import org.gradle.api.*

public abstract class ForeignPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = DefaultForeignExtension(target.objects)
        target.extensions.add(ForeignExtension::class.java, "foreign", extension)
    }
}
