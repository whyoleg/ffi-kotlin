package dev.whyoleg.foreign.gradle

import org.gradle.api.*
import org.gradle.kotlin.dsl.*

public class ForeignPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create<ForeignExtension>("foreign")
    }
}
