package dev.whyoleg.foreign.gradle

import org.gradle.api.*
import org.gradle.api.provider.*
import org.jetbrains.kotlin.gradle.plugin.*

private const val PLUGIN_ID = "dev.whyoleg.foreign.compiler"

public class ForeignCompilerGradlePlugin : KotlinCompilerPluginSupportPlugin {
    override fun apply(target: Project): Unit = with(target) {
    }

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true

    override fun getCompilerPluginId(): String = PLUGIN_ID

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = "dev.whyoleg.foreign",
        artifactId = "foreign-compiler-plugin",
        version = "0.1.0"
    )

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        val platform = kotlinCompilation.target.platformType
        val project = kotlinCompilation.target.project
        return project.provider {
            listOf(
                SubpluginOption(key = "platform", value = platform.name),
            )
        }
    }
}
