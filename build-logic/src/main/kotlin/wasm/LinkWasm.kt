package wasm

import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.model.*
import org.gradle.api.provider.*
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.*
import javax.inject.*

sealed interface LinkWasm : Task {
    @get:Input
    val compiler: Property<String>

    @get:Input
    val linkPaths: ListProperty<String>

    @get:Input
    val linkLibraries: ListProperty<String>

    @get:Input
    val includeDirs: ListProperty<String>

    @get:InputFiles
    val inputFiles: ListProperty<RegularFile>

    @get:Optional
    @get:Input
    val outputLibraryName: Property<String>

    @get:OutputDirectory
    val outputDirectory: DirectoryProperty

    @get:Internal
    val producedLibraryFile: Provider<RegularFile>
}

open class DefaultLinkWasm : LinkWasm, AbstractExecTask<DefaultLinkWasm>(DefaultLinkWasm::class.java) {
    //platform dependent
    override val compiler: Property<String> = objectFactory.property<String>().convention("emcc")
    override val linkPaths: ListProperty<String> = objectFactory.listProperty()
    override val linkLibraries: ListProperty<String> = objectFactory.listProperty()
    override val includeDirs: ListProperty<String> = objectFactory.listProperty()
    override val inputFiles: ListProperty<RegularFile> = objectFactory.listProperty<RegularFile>().convention(
        listOf(project.layout.projectDirectory.file("main/wasm/c/interop.c"))
    )
    override val outputLibraryName: Property<String> = objectFactory.property()
    override val outputDirectory: DirectoryProperty = objectFactory.directoryProperty().convention(
        project.layout.buildDirectory.dir("wasmLink")
    )

    override val producedLibraryFile: Provider<RegularFile>
        get() = outputDirectory.file(outputLibraryName.map { "$it.js" })

    override fun exec() {
        if (!outputLibraryName.isPresent) {
            logger.lifecycle("Skip WASM build")
            return
        }
        executable(compiler.get())
        args("-s", "EXPORTED_FUNCTIONS=_free")

        //link paths/libs
        args(linkPaths.get().map { "-L$it" })
        args(linkLibraries.get().map { "-l$it" })

        //include dirs
        args(includeDirs.get().map { "-I$it" })

        val output = outputDirectory.get().asFile
        output.mkdirs()
        //output
        args("-o", producedLibraryFile.get())

        //input
        args(inputFiles.get().map { it.asFile.absolutePath })

        super.exec()
    }
}

sealed interface GenerateWasmTestRunner : Task {
    @get:InputFile
    val instantiateFile: Property<String>

    @get:InputFile
    val inputLibraryFile: RegularFileProperty

    @get:Input
    val inputLibraryName: Property<String>

    @get:OutputDirectory
    val outputDirectory: DirectoryProperty

    @get:Internal
    val testRunnerFile: Provider<RegularFile>
}

open class DefaultGenerateWasmTestRunner @Inject constructor(
    objectFactory: ObjectFactory,
) : GenerateWasmTestRunner, DefaultTask() {
    override val instantiateFile: Property<String> = objectFactory.property()
    override val inputLibraryFile: RegularFileProperty = objectFactory.fileProperty()
    override val inputLibraryName: Property<String> = objectFactory.property()
    override val outputDirectory: DirectoryProperty = objectFactory.directoryProperty().convention(
        project.layout.buildDirectory.dir("wasmTestRunner")
    )

    override val testRunnerFile: Provider<RegularFile>
        get() = outputDirectory.file("run-test.mjs")

    @TaskAction
    fun generate() {
        println("GENERATE")
        val libraryName = inputLibraryName.get()
        testRunnerFile.get().asFile.run {
            parentFile.mkdirs()
            writeText(
                """|
                   |import { instantiate } from '${instantiateFile.get()}';
                   |import Module from '${inputLibraryFile.get()}';
                   |var wasmSetup = new Promise(function(resolve, reject) {
                   |     Module['onRuntimeInitialized'] = _ => {
                   |         resolve(Module);
                   |    };
                   |});
                   |await wasmSetup;
                   |export default (await instantiate({ $libraryName: Module['asm'], Module })).exports;
                   |""".trimMargin()
            )
        }
    }
}
