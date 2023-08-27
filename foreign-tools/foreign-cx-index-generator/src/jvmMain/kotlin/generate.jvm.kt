package dev.whyoleg.foreign.cx.index.generator

import dev.whyoleg.foreign.cx.index.*
import kotlinx.serialization.json.*
import okio.FileSystem
import java.nio.file.*
import kotlin.io.path.*

internal actual val SystemFileSystem: FileSystem get() = FileSystem.SYSTEM

internal actual fun generateCxIndex(
    headerFilePath: String,
    compilerArgs: List<String>
): CxIndex {
    val argumentsString = Json.encodeToString(
        GenerateCxIndexArguments.serializer(),
        GenerateCxIndexArguments(headerFilePath, compilerArgs)
    )
    val resultString = CxIndexGenerator.generate(
        argumentsString.encodeToByteArray()
    )?.decodeToString()

    val result = Json.decodeFromString(
        GenerateCxIndexResult.serializer(),
        resultString ?: error("Native execution failure: NULL")
    )

    when (result) {
        is GenerateCxIndexResult.Success -> return result.index
        is GenerateCxIndexResult.Failure -> error("Native execution failure: ${result.message ?: "No cause"}")
    }
}

internal object CxIndexGenerator {
    @JvmStatic
    external fun generate(argumentsBytes: ByteArray): ByteArray?

    init {
        currentHost().run {
            loadLibraryFromResources("clang")
            loadLibraryFromResources("CxIndexGenerator")
            loadLibraryFromResources("CxIndexGeneratorJni")
        }
    }
}

private fun currentHost(): Host {
    val os = System.getProperty("os.name").lowercase()
    val arch = System.getProperty("os.arch").lowercase()
    val isArm = arch.contains("aarch64")

    return when {
        os.contains("linux")                                                   -> when {
            isArm -> error("linux-arm64 is not supported")
            else  -> Host.LinuxX64
        }
        os.contains("mac os x") || os.contains("darwin") || os.contains("osx") -> when {
            isArm -> Host.MacOsArm64
            else  -> Host.MacOsX64
        }
        else                                                                   -> error("Unsupported OS '$os' or ARCH '$arch'")
    }
}

private enum class Host(
    private val folder: String,
    private val extension: String
) {
    MacOsArm64("macos-arm64", "dylib"),
    MacOsX64("macos-x64", "dylib"),
    LinuxX64("linux-x64", "so");

    @Suppress("UnsafeDynamicallyLoadedCode")
    fun loadLibraryFromResources(libraryName: String) {
        val resourcePath = "/libs/$folder/lib$libraryName.$extension"
        val libraryStream = checkNotNull(
            Host::class.java.getResourceAsStream(resourcePath)
        ) { "'$resourcePath' not found in resources" }
        val tempPath = Files.createTempFile("lib$libraryName-", ".$extension")
        tempPath.outputStream().use { output ->
            libraryStream.use { input ->
                input.copyTo(output)
            }
        }
        System.load(tempPath.absolutePathString())
    }
}
