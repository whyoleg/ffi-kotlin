package dev.whyoleg.ffi

import java.nio.file.*
import java.util.*
import kotlin.io.path.*

//TODO: reuse more code after libssl bindings (dependent libs)
public abstract class LibraryLoader {
    public abstract fun load()

    public companion object {
        public fun loadFromResources(path: String, suffix: String) {
            val tempFile = Files.createTempFile(path.substringAfterLast("/"), suffix)
            LibraryLoader::class.java.getResourceAsStream(path)!!.use { libraryStream ->
                tempFile.outputStream().use { tempStream ->
                    libraryStream.copyTo(tempStream)
                }
            }
            System.load(tempFile.toAbsolutePath().pathString)
        }

        internal fun init() {
            val cls = LibraryLoader::class.java
            val loader = ServiceLoader.load(cls, cls.classLoader)
            loader.forEach(LibraryLoader::load)
            loader.reload()
        }
    }

    protected enum class Platform(
        private val path: String,
        private val prefix: String,
        public val suffix: String,
    ) {
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

        public fun libraryPath(libraryName: String): String = "/libs/$path/$prefix$libraryName.$suffix"

        public companion object {
            public fun current(): Platform {
                val os = System.getProperty("os.name").lowercase()
                val arch = System.getProperty("os.arch").lowercase() //TODO: check arch
                return when {
                    os.contains("mac")                                             -> MACOS_ARM64
                    os.contains("win")                                             -> WINDOWS_X64
                    os.contains("nix") || os.contains("nux") || os.contains("aix") -> LINUX_X64
                    else                                                           -> error("Unsupported OS: $os")
                }
            }
        }
    }
}
