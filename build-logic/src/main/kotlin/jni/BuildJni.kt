package jni

import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.provider.*
import org.gradle.api.tasks.*
import org.gradle.jvm.toolchain.*
import org.gradle.kotlin.dsl.*
import javax.inject.*

sealed interface BuildJni : Task {
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
    val outputFilePath: Property<String>

    @get:OutputDirectory
    val outputDirectory: DirectoryProperty
}

open class DefaultBuildJni @Inject constructor(
    private val javaToolchainService: JavaToolchainService,
) : BuildJni, AbstractExecTask<DefaultBuildJni>(DefaultBuildJni::class.java) {
    //platform dependent
    override val compiler: Property<String> = objectFactory.property<String>().convention("clang")
    override val linkPaths: ListProperty<String> = objectFactory.listProperty()
    override val linkLibraries: ListProperty<String> = objectFactory.listProperty()
    override val includeDirs: ListProperty<String> = objectFactory.listProperty()
    override val inputFiles: ListProperty<RegularFile> = objectFactory.listProperty<RegularFile>().convention(
        listOf(project.layout.projectDirectory.file("src/jvmCommonMain/c/jni.c"))
    )
    override val outputFilePath: Property<String> = objectFactory.property()
    override val outputDirectory: DirectoryProperty = objectFactory.directoryProperty().convention(
        project.layout.buildDirectory.dir("jniLibraries")
    )

    override fun exec() {
        executable(compiler.get())
        args("-shared", "-fPIC")

        val javaHome = javaToolchainService.compilerFor {
            languageVersion.set(JavaLanguageVersion.of(8))
        }.get().metadata.installationPath.dir("include")

        //link paths/libs
        args(linkPaths.get().map { "-L$it" })
        args(linkLibraries.get().map { "-l$it" })

        //include dirs
        args((listOf(javaHome, javaHome.dir("darwin")) + includeDirs.get()).map { "-I$it" })

        //output
        args("-o", outputDirectory.get().asFile.resolve("libs").resolve(outputFilePath.get()).also { it.parentFile.mkdirs() }.absolutePath)

        //input
        args(inputFiles.get().map { it.asFile.absolutePath })

        super.exec()
    }
}
