package dev.whyoleg.foreign.gradle.internal

import dev.whyoleg.foreign.gradle.dsl.*
import dev.whyoleg.foreign.gradle.internal.cx.*
import org.gradle.api.*
import org.gradle.api.artifacts.*
import org.gradle.api.attributes.*

internal fun setupForeignPlugin(target: Project) {
    val project = ProjectPrototype.wrap(target)
    val extension = DefaultForeignExtension(project)
    target.extensions.add(ForeignExtension::class.java, "foreign", extension)

    val internalToolConfiguration = setupInternalToolConfiguration(target)

    val s: DefaultGenerateCxCompilerIndexTask = TODO()

    s.internalToolClasspath.from(internalToolConfiguration)
}

private fun setupInternalToolConfiguration(target: Project): Configuration {
    // TODO: set correct flags for configurations
    val internalToolConfiguration = target.configurations.create("foreignInternalTool") {
        it.attributes {
            it.attribute(
                Usage.USAGE_ATTRIBUTE,
                target.objects.named(Usage::class.java, Usage.JAVA_RUNTIME)
            )
        }
        it.isVisible = false
        it.isCanBeConsumed = false
    }

    target.dependencies.add(
        internalToolConfiguration.name,
        "dev.whyoleg.foreign:foreign-gradle-internal-tool:$FOREIGN_VERSION"
    )

    return internalToolConfiguration
}