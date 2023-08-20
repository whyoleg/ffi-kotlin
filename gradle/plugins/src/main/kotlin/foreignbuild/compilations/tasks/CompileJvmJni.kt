package foreignbuild.compilations.tasks

import org.gradle.api.file.*
import org.gradle.api.provider.*
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.utils.*
import java.io.*
import javax.inject.*

abstract class CompileJvmJni @Inject constructor(

) : AbstractExecTask<CompileJvmJni>(CompileJvmJni::class.java) {

    @get:Input
    val konanTargetName: Property<String> = objectFactory.property()

    @get:InputDirectory
    val jdkHeadersDirectory: DirectoryProperty = objectFactory.directoryProperty()

    @get:Internal
    val linkPaths: ListProperty<Directory> = objectFactory.listProperty()

    @get:InputFiles // for up-to-date checks
    internal val linkFiles get() = objectFactory.fileCollection().from(linkPaths)

    @get:Input
    val linkLibraries: ListProperty<String> = objectFactory.listProperty()

    @get:Internal
    val includePaths: ListProperty<Directory> = objectFactory.listProperty()

    @get:InputFiles // for up-to-date checks
    internal val includeFiles get() = objectFactory.fileCollection().from(includePaths)

    @get:InputFiles
    val inputFiles: ConfigurableFileCollection = objectFactory.fileCollection()

    @get:OutputFile
    val outputFile: RegularFileProperty = objectFactory.fileProperty()

    override fun exec() {
        val konanTargetName = konanTargetName.get()

        val includePaths = buildList {
            includePaths.get().forEach {
                add(it.asFile.absolutePath)
            }
            val javaIncludePath = jdkHeadersDirectory.get().asFile
            val platformFolder = when (konanTargetName) {
                "linux_x64"                -> "linux"
                "macos_x64", "macos_arm64" -> "darwin"
                else                       -> error("$konanTargetName is not supported")
            }
            add(javaIncludePath.absolutePath)
            add(javaIncludePath.resolve(platformFolder).absolutePath)
        }

        val outputFile = outputFile.get().asFile
        outputFile.parentFile.mkdirs()

        val runKonanPath = NativeCompilerDownloader(project).run {
            downloadIfNeeded()
            compilerDirectory.resolve("bin/run_konan").absolutePath
        }

        // task configuration

        executable(runKonanPath)
        args("clang", "clang", konanTargetName)
        args("-shared", "-fPIC") // need to set different flags for windows

        //link paths/libs
        args(linkPaths.get().map { "-L$it" })
        args(linkLibraries.get().map { "-l$it" })
        //include dirs
        args(includePaths.map { "-I$it" })

        //output
        args("-o", outputFile.absolutePath)

        //input
        args(inputFiles.files.filter(File::isFile).filter(File::exists).map { it.absolutePath })

        super.exec()
    }
}
