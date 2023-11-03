package dev.whyoleg.foreign.gradle.api

import org.gradle.api.*

public class ForeignPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply("dev.whyoleg.foreign.internal")
    }
}
