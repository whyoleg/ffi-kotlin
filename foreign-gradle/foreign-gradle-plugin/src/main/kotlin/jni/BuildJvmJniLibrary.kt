package dev.whyoleg.foreign.gradle.jni

import dev.whyoleg.foreign.gradle.*
import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.provider.*
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import org.gradle.jvm.toolchain.*
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.utils.*
import javax.inject.*

public sealed interface BuildJvmJniLibrary : Task {

    @get:Internal
    public val platform: Property<Platform.Desktop>

    //if not specified: K/N toolchain will be used
    @get:Optional
    @get:Input
    public val cCompiler: Property<String>

    @get:Input
    public val cCompilerArguments: ListProperty<String>

    @get:Nested
    public val javaCompiler: Property<JavaCompiler>

    @get:Input //TODO
    public val linkPaths: ListProperty<Directory>

    @get:Input
    public val linkLibraries: ListProperty<String>

    @get:Input //TODO
    public val includePaths: ListProperty<Directory>

    //TODO: configurable file collection
    @get:InputFiles
    public val inputFiles: ListProperty<RegularFile>

    @get:Input
    public val outputLibraryName: Property<String>

    @get:OutputDirectory
    public val outputDirectory: DirectoryProperty

    @get:Internal
    public val outputFile: Provider<RegularFile>
}

public open class DefaultBuildJvmJniLibrary @Inject constructor(
    javaToolchainService: JavaToolchainService,
) : BuildJvmJniLibrary, AbstractExecTask<DefaultBuildJvmJniLibrary>(DefaultBuildJvmJniLibrary::class.java) {
    final override val platform: Property<Platform.Desktop> = objectFactory.property()

    @get:Input
    internal val platformString: Provider<String> get() = platform.map { it.toString() }

    final override val cCompiler: Property<String> = objectFactory.property()
    final override val cCompilerArguments: ListProperty<String> = objectFactory.listProperty()
    final override val javaCompiler: Property<JavaCompiler> = objectFactory.property<JavaCompiler>().convention(
        javaToolchainService.compilerFor {
            languageVersion.set(JavaLanguageVersion.of(8))
        }
    )
    final override val linkPaths: ListProperty<Directory> = objectFactory.listProperty()
    final override val linkLibraries: ListProperty<String> = objectFactory.listProperty()
    final override val includePaths: ListProperty<Directory> = objectFactory.listProperty()
    final override val inputFiles: ListProperty<RegularFile> = objectFactory.listProperty()
    final override val outputLibraryName: Property<String> = objectFactory.property()
    final override val outputDirectory: DirectoryProperty = objectFactory.directoryProperty()

    final override val outputFile: Provider<RegularFile> = outputDirectory.file(outputLibraryName.flatMap { libraryName ->
        platform.map { platform ->
            platform.sharedLibraryName(libraryName)
        }
    })

    private val nativeCompilerDownloader = NativeCompilerDownloader(project)

    override fun exec() {
        val platform = platform.get()
        // for cross-compiling we need to get jni headers for every OS (macos, linux, windows)
        check(platform == Platform.Host) { "only host supported for now" }
        when (val cCompiler = cCompiler.orNull) {
            null -> {
                val konanTargetName = when (platform) {
                    Platform.Linux.X64   -> "linux_x64"
                    Platform.MacOS.ARM64 -> "macos_arm64"
                    Platform.MacOS.X64   -> "macos_x64"
                    Platform.Windows.X64 -> TODO("windows is not supported")
                }
                nativeCompilerDownloader.downloadIfNeeded()
                executable(nativeCompilerDownloader.compilerDirectory.resolve("bin/run_konan").absolutePath)
                args("clang", "clang", konanTargetName)
            }
            else -> executable(cCompiler)
        }

        when (platform) {
            is Platform.Windows -> args("-shared")
            else                -> args("-shared", "-fPIC")
        }

        args(cCompilerArguments.get())

        //link paths/libs
        args(linkPaths.get().map { "-L$it" })
        args(linkLibraries.get().map { "-l$it" })

        val javaHome = javaCompiler.get().metadata.installationPath.dir("include")
        val javaHomePlatformFolder = when (platform) {
            is Platform.Linux   -> "linux"
            is Platform.Windows -> "win32"
            is Platform.MacOS   -> "darwin"
        }

        //include dirs
        args((listOf(javaHome, javaHome.dir(javaHomePlatformFolder)) + includePaths.get()).map { "-I$it" })

        //output
        args("-o", outputFile.get().asFile.also { it.parentFile.mkdirs() }.absolutePath)

        //input //TODO: check that at least file exists
        args(inputFiles.get().map { it.asFile.absolutePath })

        super.exec()
    }
}
