package dev.whyoleg.foreign.gradle

import dev.whyoleg.foreign.gradle.api.*
import dev.whyoleg.foreign.gradle.interfaces.*
import org.gradle.api.*
import org.gradle.kotlin.dsl.*

public class ForeignPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create(
            publicType = ForeignExtension::class,
            name = "foreign",
            instanceType = DefaultForeignExtension::class
        )

        extension.interfaces.all {
            when (it) {
                is DefaultRootCxForeignInterfaceConfiguration -> {
                    it.platforms.all {
                        when (it) {
                            is DefaultJvmPlatformCxForeignInterfaceConfiguration -> {
                                it.hosts.all {
                                    it.registerGenerateIndexTask(target.tasks)
                                }
                            }
                        }
                    }
                }
                else                                          -> TODO()
            }
        }
    }
}
