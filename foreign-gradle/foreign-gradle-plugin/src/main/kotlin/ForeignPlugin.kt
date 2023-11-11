package dev.whyoleg.foreign.gradle

import dev.whyoleg.foreign.gradle.internal.*
import org.gradle.api.*

public class ForeignPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = setupForeignPlugin(target)
}
