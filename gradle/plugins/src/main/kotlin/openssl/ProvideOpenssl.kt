package openssl

import org.gradle.api.*
import org.gradle.api.provider.*
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.konan.target.*
import java.io.*

open class OpensslExtension(
    @Transient val rootProject: Project,
) {

    val prepareOpensslTaskProvider = rootProject.tasks.named<Sync>(OpensslRootPlugin.PREPARE_OPENSSL_TASK_NAM)

    fun includeDir(target: String): Provider<File> {
        return prepareOpensslTaskProvider.map { it.destinationDir.resolve(target).resolve("include") }
    }

    fun libDir(target: String): Provider<File> {
        return prepareOpensslTaskProvider.map { it.destinationDir.resolve(target).resolve("lib") }
    }
}

private fun KonanTarget.opensslTarget(): String = when (this) {
    KonanTarget.IOS_ARM32           -> "ios-device-arm32"
    KonanTarget.IOS_ARM64           -> "ios-device-arm64"
    KonanTarget.IOS_SIMULATOR_ARM64 -> "ios-simulator-arm64"
    KonanTarget.IOS_X64             -> "ios-simulator-x64"
    KonanTarget.LINUX_X64           -> "linux-x64"
    KonanTarget.MACOS_ARM64         -> "macos-arm64"
    KonanTarget.MACOS_X64           -> "macos-x64"
    else                            -> TODO("NOT SUPPORTED")
}

fun OpensslExtension.includeDir(target: KonanTarget): Provider<File> = includeDir(target.opensslTarget())
fun OpensslExtension.libDir(target: KonanTarget): Provider<File> = libDir(target.opensslTarget())
