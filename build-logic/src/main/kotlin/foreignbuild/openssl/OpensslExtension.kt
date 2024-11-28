package foreignbuild.openssl

import org.gradle.api.file.*
import org.gradle.api.provider.*
import org.gradle.api.tasks.*
import org.jetbrains.kotlin.konan.target.*

@Suppress("PropertyName")
class OpensslExtension(
    val v3_0: OpensslXExtension,
    val v3_1: OpensslXExtension,
    val v3_2: OpensslXExtension,
)

class OpensslXExtension(
    val setupTask: TaskProvider<UnzipTask>,
) {
    private val directory: Provider<Directory> = setupTask.map { it.outputDirectory.get() }

    fun libDirectory(target: KonanTarget): Provider<Directory> = libDirectory(opensslTarget(target))
    fun includeDirectory(target: KonanTarget): Provider<Directory> = includeDirectory(opensslTarget(target))

    // exists only for windows-x64
    fun binDirectory(target: String) = directory.map { it.dir("$target/bin") }
    private fun libDirectory(target: String) = directory.map { it.dir("$target/lib") }
    private fun includeDirectory(target: String) = directory.map { it.dir("$target/include") }
    private fun opensslTarget(target: KonanTarget): String = when (target) {
        KonanTarget.IOS_ARM64               -> "ios-device-arm64"
        KonanTarget.IOS_SIMULATOR_ARM64     -> "ios-simulator-arm64"
        KonanTarget.IOS_X64                 -> "ios-simulator-x64"
        KonanTarget.TVOS_ARM64              -> "tvos-device-arm64"
        KonanTarget.TVOS_X64                -> "tvos-simulator-x64"
        KonanTarget.TVOS_SIMULATOR_ARM64    -> "tvos-simulator-arm64"
        KonanTarget.WATCHOS_ARM32           -> "watchos-device-arm32"
        KonanTarget.WATCHOS_ARM64           -> "watchos-device-arm64_32"
        KonanTarget.WATCHOS_DEVICE_ARM64    -> "watchos-device-arm64"
        KonanTarget.WATCHOS_X64             -> "watchos-simulator-x64"
        KonanTarget.WATCHOS_SIMULATOR_ARM64 -> "watchos-simulator-arm64"
        KonanTarget.LINUX_X64               -> "linux-x64"
        KonanTarget.LINUX_ARM64             -> "linux-arm64"
        KonanTarget.MACOS_ARM64             -> "macos-arm64"
        KonanTarget.MACOS_X64               -> "macos-x64"
        KonanTarget.MINGW_X64               -> "mingw-x64"
        KonanTarget.ANDROID_X64             -> "android-x64"
        KonanTarget.ANDROID_X86             -> "android-x86"
        KonanTarget.ANDROID_ARM32           -> "android-arm32"
        KonanTarget.ANDROID_ARM64           -> "android-arm64"
        else                                -> error("$target is not supported by OpenSSL")
    }
}
