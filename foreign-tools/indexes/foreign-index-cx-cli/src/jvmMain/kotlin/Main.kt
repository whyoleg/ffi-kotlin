package dev.whyoleg.foreign.index.cx.cli

import java.nio.file.*
import kotlin.io.path.*
import kotlin.system.*

private val testArgs = arrayOf(
    "--verbose",
    "--header", "openssl/evp.h",
    "--header", "openssl/err.h",
    "--header", "openssl/encoder.h",
    "--header", "openssl/decoder.h",
    "--header", "openssl/ec.h",

    "--include", "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/build/openssl/prebuilt/macos-arm64/include",
    "--include", "/Library/Developer/CommandLineTools/SDKs/MacOSX.sdk/System/Library/Frameworks/Kernel.framework/Headers/",
    "--include", "/Library/Developer/CommandLineTools/SDKs/MacOSX.sdk/usr/include",
    "--output", "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/build/foreign/libcrypto3.json",
)

fun main(args: Array<String>) {
    run(args)
}

private fun run(args: Array<String>) {
    val host = currentHost()

    // TODO: what is the folder should be here?
    val rootDir = Path(System.getProperty("user.home")).resolve(".foreign-kotlin")
    val cliDir = rootDir.resolve("foreign-index-cx-cli")
    val libDir = cliDir.resolve("lib").createDirectories()
    val binDir = cliDir.resolve("bin").createDirectories()

    libDir.resolve(host.libraryName).apply {
        deleteIfExists() // TODO cache by hash
        loadFromResources("/cli/${host.folder}/lib/${host.libraryName}")
    }

    val kexePath = binDir.resolve("cli.kexe").apply {
        deleteIfExists() // TODO cache by hash
        loadFromResources("/cli/${host.folder}/bin/foreign-index-cx-cli.kexe")
        toFile().setExecutable(true)
    }.absolutePathString()

    exitProcess(
        ProcessBuilder(kexePath, *args).apply {
            environment()[host.libraryPathEnvironmentVariableName] = libDir.absolutePathString()
        }.inheritIO().start().waitFor()
    )
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
    val folder: String,
    val libraryName: String,
    val libraryPathEnvironmentVariableName: String
) {
    MacOsArm64("macos-arm64", "libclang.dylib", "DYLD_LIBRARY_PATH"),
    MacOsX64("macos-x64", "libclang.dylib", "DYLD_LIBRARY_PATH"),
    LinuxX64("linux-x64", "libclang.so", "LD_LIBRARY_PATH");
}

private fun Path.loadFromResources(path: String) {
    checkNotNull(Host::class.java.getResourceAsStream(path)) {
        "'$path' not found in resources"
    }.use { input ->
        outputStream().use { output ->
            input.copyTo(output)
        }
    }
}
