import foreignbuild.*
import org.jetbrains.kotlin.gradle.targets.native.toolchain.*
import org.jetbrains.kotlin.konan.target.*
import org.jetbrains.kotlin.konan.target.Distribution

plugins {
    id("foreignbuild.kotlin-tool")

    id("foreignbuild.setup-openssl")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.foreignToolClang)
        }
    }
}

registerGenerateConstantsTask(
    "generateTestenvConstants",
    TestenvGenerateConstantsTask::class,
    kotlin.sourceSets.commonMain
) {
    packageName = "dev.whyoleg.foreign.tool.clang.testenv"
    className = "TestenvConstants"
    properties.put(
        "OPENSSL3_ROOT_PATH",
        openssl.v3_2.setupTask.map { it.outputDirectory.get().asFile.absolutePath }
    )
    properties.putAll(toolchainProperties)
}

// `UsesKotlinNativeBundleBuildService` and `KotlinNativeProvider` are `internal` KGP APIs
@Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")
abstract class TestenvGenerateConstantsTask : GenerateConstantsTask(), UsesKotlinNativeBundleBuildService {

    @get:Nested
    internal val kotlinNativeProvider: Provider<KotlinNativeFromToolchainProvider> = project.provider {
        KotlinNativeFromToolchainProvider(
            project = project,
            konanTargets = setOf(
                // for now, let's download all desktop native deps
                KonanTarget.LINUX_X64,
                KonanTarget.LINUX_ARM64,
                KonanTarget.MACOS_ARM64,
                KonanTarget.MACOS_X64,
                KonanTarget.MINGW_X64,
            ),
            kotlinNativeBundleBuildService = kotlinNativeBundleBuildService
        )
    }

    @get:Internal
    internal val toolchainProperties: Provider<Map<String, String>> = kotlinNativeProvider.map {
        val distribution = Distribution(
            konanHome = it.bundleDirectory.get().asFile.absolutePath,
            konanDataDir = it.konanDataDir.orNull
        )
        mapOf(
            "LLVM_PATH" to distribution.dependencyPath("llvm.${HostManager.host.name}.user"),
            "MINGW_X64_TOOLCHAIN_PATH" to distribution.toolchainDependencyPath(KonanTarget.MINGW_X64),
            "LINUX_X64_TOOLCHAIN_PATH" to distribution.toolchainDependencyPath(KonanTarget.LINUX_X64),
            "LINUX_ARM64_TOOLCHAIN_PATH" to distribution.toolchainDependencyPath(KonanTarget.LINUX_ARM64),
        )
    }

    private fun Distribution.toolchainDependencyPath(target: KonanTarget): String {
        return dependencyPath("toolchainDependency.${target.name}")
    }

    private fun Distribution.dependencyPath(propertyName: String): String {
        val dependency = requireNotNull(properties.getProperty(propertyName)) {
            "No property $propertyName found in konan.properties"
        }
        return File(dependenciesDir).resolve(dependency).absolutePath
    }
}
