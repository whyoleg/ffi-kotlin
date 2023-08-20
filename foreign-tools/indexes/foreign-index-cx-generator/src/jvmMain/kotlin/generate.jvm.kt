package dev.whyoleg.foreign.index.cx.generator

import dev.whyoleg.foreign.index.cx.*
import kotlinx.serialization.json.*
import java.nio.file.*
import kotlin.io.path.*

public actual fun CxIndex.Companion.generate(
    headers: Set<String>,
    includePaths: Set<String>
): CxIndex {
    val arguments = CxIndexArguments(
        headers = headers,
        includePaths = includePaths
    )
    val argumentsString = Json.encodeToString(CxIndexArguments.serializer(), arguments)
    val resultPath = Files.createTempFile("CxIndexGenerator-result-", ".json")
    CxIndexGenerator.generate(
        argumentsString,
        resultPath.absolutePathString()
    )
    val result = if (resultPath.exists()) {
        Json.decodeFromString(CxIndexResult.serializer(), resultPath.readText())
    } else null
    result?.index?.let {
        resultPath.deleteIfExists()
        return it
    }
    error("Native execution failure: ${result?.error ?: ""}")
}

internal object CxIndexGenerator {
    @JvmStatic
    external fun generate(
        argumentsString: String,
        resultPathString: String
    )

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
