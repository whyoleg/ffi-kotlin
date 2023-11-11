package dev.whyoleg.foreign.gradle.internal.cx

import dev.whyoleg.foreign.gradle.internal.tool.*
import dev.whyoleg.foreign.gradle.tasks.*
import dev.whyoleg.foreign.gradle.tooling.*
import org.gradle.api.file.*
import org.gradle.api.model.*
import org.gradle.api.provider.*
import org.gradle.api.tasks.*
import org.gradle.workers.*
import javax.inject.*

internal abstract class DefaultGenerateCxCompilerIndexTask @Inject constructor(
    objects: ObjectFactory
) : BuildCxCompilerIndexTask {
    override val destinationDirectory: DirectoryProperty = objects.directoryProperty()
    override val nativeTarget: Property<NativeTarget> = objects.property(NativeTarget::class.java)
    override val toolchainDependencies: MapProperty<ToolchainDependency, FileSystemLocation> =
        objects.mapProperty(ToolchainDependency::class.java, FileSystemLocation::class.java)

    @get:InputFiles
    val internalToolClasspath: ConfigurableFileCollection = objects.fileCollection()

    @get:Inject
    abstract val workerExecutor: WorkerExecutor

    @TaskAction
    fun generate() {
        workerExecutor.classLoaderIsolation {
            it.classpath.from(internalToolClasspath)
        }.submit(GenerateCxCompilerIndexAction::class.java) {
            // TODO: other properties
            it.target.set(nativeTarget)
            it.dependencies.set(toolchainDependencies)
        }
    }
}

// only it can access ForeignGradleTool
internal interface GenerateCxCompilerIndexAction : WorkAction<GenerateCxCompilerIndexAction.Parameters> {
    interface Parameters : WorkParameters {
        val inputFile: RegularFileProperty
        val outputFile: RegularFileProperty
        val target: Property<NativeTarget>
        val dependencies: MapProperty<ToolchainDependency, FileSystemLocation>
        val additionalCompilerArguments: ListProperty<String>
    }

    override fun execute(): Unit = with(parameters) {
        ForeignGradleTool.buildIndex(
            inputFile = inputFile.get().asFile,
            outputFile = outputFile.get().asFile,
            target = target.get(),
            dependencies = dependencies.get().mapValues { it.value.asFile },
            additionalCompilerArguments = additionalCompilerArguments.get()
        )
    }
}
