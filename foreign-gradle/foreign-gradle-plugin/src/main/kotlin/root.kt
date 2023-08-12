package dev.whyoleg.foreign.gradle

import org.gradle.internal.os.*

public sealed interface Platform {
    public fun sharedLibraryName(libraryName: String): String

    public sealed interface Desktop : Platform

    public sealed interface ARM64 : Platform
    public sealed interface X64 : Platform

    public sealed class Host : Desktop {
        protected abstract val os: OperatingSystem
        override fun sharedLibraryName(libraryName: String): String = os.getSharedLibraryName(libraryName)
    }

    public sealed class MacOS : Host() {
        override val os: OperatingSystem = OperatingSystem.forName("darwin")

        public object X64 : MacOS(), Platform.X64 {
            override fun toString(): String = "macos-x64"
        }

        public object ARM64 : MacOS(), Platform.ARM64 {
            override fun toString(): String = "macos-arm64"
        }
    }

    public sealed class Linux : Host() {
        override val os: OperatingSystem = OperatingSystem.forName("linux")

        public object X64 : Linux(), Platform.X64 {
            override fun toString(): String = "linux-x64"
        }
    }

    public sealed class Windows : Host() {
        override val os: OperatingSystem = OperatingSystem.forName("windows")

        public object X64 : Windows(), Platform.X64 {
            override fun toString(): String = "windows-x64"
        }
    }

    public companion object {
        public val Host: Host = hostPlatform()
    }
}

private fun hostPlatform(): Platform.Host {
    val os = System.getProperty("os.name").lowercase()
    val arch = System.getProperty("os.arch").lowercase()
    val isArm = arch.contains("aarch64")
    if (os.contains("windows")) {
        if (!isArm) return Platform.Windows.X64
    }
    if (os.contains("linux")) {
        if (!isArm) return Platform.Linux.X64
    }
    if (os.contains("mac os x") || os.contains("darwin") || os.contains("osx")) {
        if (!isArm) return Platform.MacOS.X64
        return Platform.MacOS.ARM64
    }
    error("Unsupported OS '$os' or ARCH '$arch'")
}
