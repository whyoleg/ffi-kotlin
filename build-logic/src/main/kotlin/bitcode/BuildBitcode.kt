package bitcode

import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.provider.*
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.*
import javax.inject.*

sealed interface BuildBitcode : Task {
    @get:Input
    val runKonanPath: Property<String>

    @get:Input
    val konanTarget: Property<String>

    @get:Input
    val includeDirs: ListProperty<String>

    @get:InputFiles
    val inputFile: RegularFileProperty

    @get:Optional
    @get:Input
    val outputFilePath: Property<String>

    @get:OutputDirectory
    val outputDirectory: DirectoryProperty

    @get:Internal
    val outputFile: Provider<RegularFile>
        get() = outputDirectory.flatMap { it.file(outputFilePath) }
}

open class DefaultBuildBitcode @Inject constructor(
) : BuildBitcode, AbstractExecTask<DefaultBuildBitcode>(DefaultBuildBitcode::class.java) {
    //platform dependent
    override val runKonanPath: Property<String> = objectFactory.property<String>().convention(
        "/Users/whyoleg/.konan/kotlin-native-prebuilt-macos-aarch64-1.8.20/bin/run_konan"
    )
    override val konanTarget: Property<String> = objectFactory.property<String>()
    override val includeDirs: ListProperty<String> = objectFactory.listProperty()
    override val inputFile: RegularFileProperty = objectFactory.fileProperty().convention(
        project.layout.projectDirectory.file("src/nativeMain/c/interop.c")
    )
    override val outputFilePath: Property<String> = objectFactory.property()
    override val outputDirectory: DirectoryProperty = objectFactory.directoryProperty().convention(
        project.layout.buildDirectory.dir("nativeBitcode")
    )

    override fun exec() {
        executable(runKonanPath.get())
        args("clang", "clang", konanTarget.get(), "-emit-llvm")

        //include dirs
        args(includeDirs.get().map { "-I$it" })

        //output
        args("-o", outputFile.get().asFile.also { it.parentFile.mkdirs() }.absolutePath)

        //input
        args("-c", inputFile.get().asFile.absolutePath)

        super.exec()
    }
}
