package dev.whyoleg.foreign.memory

import java.nio.file.*
import kotlin.io.path.*

public sealed interface EmbeddedLibraryLoader {
    public fun loadLibrary(libraryName: String)

    public companion object {
        public val Current: EmbeddedLibraryLoader
            get() {
                if (System.getProperty("java.vendor")!!.contains("android", ignoreCase = true)) return Android

                val os = System.getProperty("os.name")!!.lowercase()
                val arch = System.getProperty("os.arch")!!.lowercase()
                return when {
                    os.contains("mac") && arch.contains("aarch64")                 -> JVM.MACOS_ARM64
                    os.contains("win")                                             -> JVM.WINDOWS_X64
                    os.contains("nix") || os.contains("nux") || os.contains("aix") -> JVM.LINUX_X64
                    else                                                           -> error("Unsupported OS: $os")
                }
            }
    }

    public object Android : EmbeddedLibraryLoader {
        override fun loadLibrary(libraryName: String) {
            System.loadLibrary(libraryName)
        }
    }

    public enum class JVM(
        private val path: String,
        private val prefix: String,
        private val suffix: String,
    ) : EmbeddedLibraryLoader {
        MACOS_ARM64(
            path = "macos-arm64",
            prefix = "lib",
            suffix = "dylib"
        ),
        LINUX_X64(
            path = "linux-x64",
            prefix = "lib",
            suffix = "so"
        ),
        WINDOWS_X64(
            path = "windows-X64",
            prefix = "",
            suffix = "dll"
        );

        override fun loadLibrary(libraryName: String) {
            try {
                System.loadLibrary(libraryName)
            } catch (systemCause: Throwable) {
                try {
                    loadFromResources(libraryName)
                } catch (cause: Throwable) {
                    cause.addSuppressed(systemCause)
                    throw cause
                }
            }
        }

        private fun loadFromResources(libraryName: String) {
            val tempFile = Files.createTempFile(libraryName, suffix)
            EmbeddedLibraryLoader::class.java.getResourceAsStream("/libs/$path/$prefix$libraryName.$suffix")!!.use { libraryStream ->
                tempFile.outputStream().use { tempStream ->
                    libraryStream.copyTo(tempStream)
                }
            }
            System.load(tempFile.toAbsolutePath().pathString)
        }
    }
}
