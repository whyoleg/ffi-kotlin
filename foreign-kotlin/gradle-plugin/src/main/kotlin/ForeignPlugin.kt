package dev.whyoleg.foreign.gradle.plugin

import dev.whyoleg.foreign.gradle.plugin.internal.*
import org.gradle.api.*

internal abstract class ForeignPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = DefaultForeignExtension(target.objects)
        target.extensions.add("foreign", extension)
    }
}
