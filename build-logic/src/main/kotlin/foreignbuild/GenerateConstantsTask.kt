package foreignbuild

import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.provider.*
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.plugin.*

fun Project.registerGenerateConstantsTask(
    name: String,
    sourceSet: NamedDomainObjectProvider<KotlinSourceSet>,
    configure: GenerateConstantsTask.() -> Unit
): TaskProvider<GenerateConstantsTask> {
    val task = tasks.register<GenerateConstantsTask>(name) {
        destinationDir = temporaryDir
        configure()
    }

    tasks.maybeCreate("prepareKotlinIdeaImport").dependsOn(task)
    sourceSet.configure { kotlin.srcDir(task) }
    return task
}

abstract class GenerateConstantsTask : DefaultTask() {

    @get:OutputDirectory
    abstract val destinationDir: DirectoryProperty

    @get:Input
    abstract val properties: MapProperty<String, String>

    @get:Input
    abstract val packageName: Property<String>

    @get:Input
    abstract val className: Property<String>

    @TaskAction
    fun action() {
        val properties = properties.get()
        val destinationDirectory = destinationDir.get().asFile

        val fileContent = buildString {
            appendLine("package ${packageName.get()}")
            appendLine("internal object ${className.get()} {")
            properties.forEach { (k, v) ->
                appendLine("""val $k = "$v"""")
            }
            appendLine("}")
        }

        destinationDirectory.deleteRecursively()
        destinationDirectory.mkdirs()
        destinationDirectory.resolve("${className.get()}.kt").writeText(fileContent)
    }
}