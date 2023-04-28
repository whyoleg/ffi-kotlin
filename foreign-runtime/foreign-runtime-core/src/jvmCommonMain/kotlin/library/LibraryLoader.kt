package dev.whyoleg.foreign.library

import dev.whyoleg.foreign.memory.*
import java.nio.file.*
import kotlin.io.path.*

@ForeignMemoryApi
public object LibraryLoader {
    private val loaded = mutableSetOf<String>()

    public fun requireLoaded(vararg libraries: String) {
        libraries.forEach { libraryName ->
            if (libraryName in loaded) return@forEach

            when (val provider = LibraryProvider.providers.getValue(libraryName)) {
                is LibraryProvider.Shared   -> loadShared(libraryName)
                is LibraryProvider.Embedded -> loadEmbedded(provider.libraryNameFor(OS.CURRENT), provider.static)
            }
        }
    }

    public fun loadFromResources(libraryName: String, libraryPath: String) {
        if (libraryName in loaded) return

        val tempFile = Files.createTempFile("foreign-kotlin-$libraryName", "library")
        LibraryLoader::class.java.getResourceAsStream("/libs/$libraryPath")!!.use { libraryStream ->
            tempFile.outputStream().use { tempStream ->
                libraryStream.copyTo(tempStream)
            }
        }
        System.load(tempFile.absolutePathString())

        loaded += libraryName
    }

    public fun loadShared(libraryName: String) {
        if (libraryName in loaded) return

        System.loadLibrary(libraryName)

        loaded += libraryName
    }

    public fun loadEmbedded(libraryName: String, static: Boolean) {
        if (libraryName in loaded) return

        when (val os = OS.CURRENT) {
            OS.ANDROID    -> loadShared(libraryName)
            is OS.DESKTOP -> {
                try {
                    loadShared(libraryName)
                } catch (systemCause: Throwable) {
                    try {
                        val fileName = when (static) {
                            true  -> "lib$libraryName.a"
                            false -> os.sharedLibraryName(libraryName)
                        }
                        loadFromResources(libraryName, fileName)
                    } catch (cause: Throwable) {
                        cause.addSuppressed(systemCause)
                        throw cause
                    }
                }
            }
        }
    }

    public sealed class OS {
        public object ANDROID : OS()
        public sealed class DESKTOP : OS() {
            protected abstract val libraryPrefix: String
            protected abstract val libraryExtension: String

            internal abstract val libraryPath: String
            internal fun sharedLibraryName(libraryName: String): String = "$libraryPrefix$libraryName.$libraryExtension"
        }

        public sealed class MACOS : DESKTOP() {
            override val libraryPrefix: String get() = "lib"
            override val libraryExtension: String get() = "dylib"

            public object ARM64 : MACOS() {
                override val libraryPath: String get() = "macos-arm64"
            }

            public object X64 : MACOS() {
                override val libraryPath: String get() = "macos-x64"
            }
        }

        public sealed class WINDOWS : DESKTOP() {
            override val libraryPrefix: String get() = ""
            override val libraryExtension: String get() = "dll"

            public object X64 : WINDOWS() {
                override val libraryPath: String get() = "windows-x64"
            }
        }

        public sealed class LINUX : DESKTOP() {
            override val libraryPrefix: String get() = "lib"
            override val libraryExtension: String get() = "so"

            public object X64 : LINUX() {
                override val libraryPath: String get() = "linux-x64"
            }
        }

        public companion object {
            public val CURRENT: OS = run {
                val vendor = System.getProperty("java.vendor")!!.lowercase()
                val os = System.getProperty("os.name")!!.lowercase()
                val arch = System.getProperty("os.arch")!!.lowercase()
                when {
                    vendor.contains("android")                                     -> ANDROID
                    os.contains("mac") && arch.contains("aarch64")                 -> MACOS.ARM64
                    os.contains("mac")                                             -> MACOS.X64
                    os.contains("win")                                             -> WINDOWS.X64
                    os.contains("nix") || os.contains("nux") || os.contains("aix") -> LINUX.X64
                    else                                                           -> error("Unsupported OS: $os")
                }
            }
        }
    }
}
