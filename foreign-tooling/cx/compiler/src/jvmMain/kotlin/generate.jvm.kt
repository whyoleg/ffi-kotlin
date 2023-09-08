package dev.whyoleg.foreign.tooling.cx.compiler

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.nio.file.*
import kotlin.io.path.*

internal actual fun CxIndexGenerator.generate(arguments: CxIndexGenerator.Arguments): CxIndexGenerator.Result {
    val argumentsString = Json.encodeToString<CxIndexGenerator.Arguments>(arguments)
    val resultString = CxIndexGeneratorJni.generateCxIndexBridge(argumentsString.encodeToByteArray())?.decodeToString()
    return Json.decodeFromString<CxIndexGenerator.Result>(resultString ?: error("Native execution failure: NULL"))
}

internal object CxIndexGeneratorJni {
    @JvmStatic
    external fun generateCxIndexBridge(argumentsBytes: ByteArray): ByteArray?

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

    // TODO: somehow cache and not extract every time
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
