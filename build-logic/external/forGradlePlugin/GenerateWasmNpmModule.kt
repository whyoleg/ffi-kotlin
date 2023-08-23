package wasm

import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.model.*
import org.gradle.api.provider.*
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.*
import javax.inject.*

sealed interface GenerateWasmNpmModule : Task {
    @get:InputDirectory
    val inputLibraryDirectory: DirectoryProperty

    @get:Input
    val inputLibraryName: Property<String>

    @get:OutputDirectory
    val outputDirectory: DirectoryProperty
}

fun TaskContainer.registerGenerateWasmNpmModuleTask(block: GenerateWasmNpmModule.() -> Unit): TaskProvider<out GenerateWasmNpmModule> =
    register("generateWasmNpmModule", DefaultGenerateWasmNpmModule::class) {
        block()
        from(inputLibraryDirectory) {
            rename {
                when (it) {
                    inputLibraryName.get() -> "index.js"
                    else                   -> it
                }
            }
        }
        into(outputDirectory)
    }

open class DefaultGenerateWasmNpmModule @Inject constructor(
    objectFactory: ObjectFactory,
) : GenerateWasmNpmModule, Sync() {
    override val inputLibraryDirectory: DirectoryProperty = objectFactory.directoryProperty()
    override val inputLibraryName: Property<String> = objectFactory.property()
    override val outputDirectory: DirectoryProperty = objectFactory.directoryProperty().convention(
        project.layout.buildDirectory.dir("wasmNpm")
    )
}
