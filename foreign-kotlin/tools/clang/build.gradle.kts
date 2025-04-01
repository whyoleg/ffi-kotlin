import foreignbuild.*
import org.jetbrains.kotlin.gradle.*
import org.jetbrains.kotlin.gradle.plugin.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.gradle.targets.native.tasks.*
import org.jetbrains.kotlin.konan.target.*

plugins {
    id("foreignbuild.kotlin-tool")
    alias(libs.plugins.kotlin.plugin.serialization)

    id("foreignbuild.setup-libclang")
    id("foreignbuild.setup-openssl")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.foreignToolClangApi)

            implementation(projects.foreignToolSerialization)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

// cinterop opt-in
// it's not possible to `opt-in` just for native part....
kotlin {
    sourceSets {
        listOf(
            nativeMain, appleMain, macosMain, linuxMain, mingwMain,
            nativeTest, appleTest, macosTest, linuxTest, mingwTest
        ).forEach {
            it.languageSettings { optIn(OptIns.ExperimentalForeignApi) }
        }
    }
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    targets.withType<KotlinNativeTarget>().configureEach {
        compilerOptions.optIn.add(OptIns.ExperimentalForeignApi)
    }
}

// cinterop with libclang setup
kotlin.targets.withType<KotlinNativeTargetWithHostTests>().configureEach {
    compilations.named("main") {
        cinterops.create("clang") {
            defFile("src/nativeMain/interop/clang.def")
            includeDirs("src/nativeMain/interop/include")
        }
    }

    val setupClangLibsTask = setupClangLibsTask(konanTarget)
    binaries.configureEach {
        linkTaskProvider.configure { dependsOn(setupClangLibsTask) }
        linkerOpts("-L${setupClangLibsTask.get().destinationDir}")
    }
    testRuns.configureEach {
        @Suppress("UNCHECKED_CAST")
        (this as ExecutionTaskHolder<KotlinNativeHostTest>).executionTask.configure {
            dependsOn(setupClangLibsTask)
            setLibraryPath(konanTarget, ::environment)
        }
    }
}

// native -> JVM cli providing
kotlin {
    targets.withType<KotlinNativeTargetWithHostTests>().configureEach {
        binaries.executable { entryPoint("dev.whyoleg.foreign.tool.clang.main") }
    }

    jvm {
        testRuns.configureEach {
            executionTask.configure {
                setLibraryPath(HostManager.host, ::environment)
                environment("DEV_WHYOLEG_FOREIGN_TOOLING_DIRECTORY", temporaryDir.resolve("tooling"))
            }
        }
    }

    val clangBuildType = providers.gradleProperty("foreignbuild.clang.buildType").map {
        NativeBuildType.valueOf(it.uppercase())
    }.orNull ?: NativeBuildType.RELEASE

    val copyClangLibs by tasks.registering(Sync::class) {
        destinationDir = temporaryDir

        targets.withType<KotlinNativeTargetWithHostTests>().all {
            val executable = binaries.getExecutable(clangBuildType)
            dependsOn(executable.linkTaskProvider)
            from(executable.outputDirectoryProperty) { into(name) }
        }
    }
    sourceSets.jvmMain {
        // TODO: include libclang.* inside
        resources.srcDir(copyClangLibs)
    }
}

// generateConstants setup
registerGenerateConstantsTask("generateClangTestConstants", kotlin.sourceSets.commonTest) {
    packageName = "dev.whyoleg.foreign.tool.clang"
    className = "TestConstants"
    properties.put(
        "OPENSSL3_ROOT_PATH",
        openssl.v3_2.setupTask.map { it.outputDirectory.get().asFile.absolutePath }
    )
}

fun setupClangLibsTask(konanTarget: KonanTarget) = when (konanTarget) {
    KonanTarget.MACOS_ARM64 -> tasks.setupClangLibsMacosArm64
    KonanTarget.MACOS_X64   -> tasks.setupClangLibsMacosX64
    KonanTarget.LINUX_X64   -> tasks.setupClangLibsLinuxX64
    KonanTarget.MINGW_X64   -> tasks.setupClangLibsMingwX64
    else                    -> error("Not supported: $konanTarget")
}

fun setLibraryPath(konanTarget: KonanTarget, environment: (key: String, value: String) -> Unit) {
    val libraryPath = setupClangLibsTask(konanTarget).get().destinationDir.absolutePath
    when (konanTarget.family) {
        Family.OSX -> environment("DYLD_LIBRARY_PATH", libraryPath)
        Family.LINUX -> environment("LD_LIBRARY_PATH", libraryPath)
        Family.MINGW -> environment("PATH", "$libraryPath;${providers.environmentVariable("PATH").get()}")
        else -> error("not supported: $konanTarget")
    }
}
