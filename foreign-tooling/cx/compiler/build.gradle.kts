import foreignbuild.jni.*
import org.jetbrains.kotlin.gradle.plugin.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.gradle.targets.native.tasks.*
import org.jetbrains.kotlin.gradle.tasks.*
import org.jetbrains.kotlin.konan.target.*

plugins {
    id("foreignbuild.multiplatform-library")
    id("foreignbuild.desktop-tool")
    id("foreignbuild.setup-libclang")
    id("foreignbuild.setup-jdk")
    alias(kotlinLibs.plugins.plugin.serialization)
}

// common configuration
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.cx.foreignToolingCxCompilerModel)
                // for K/JVM->K/N bridge via JNI
                implementation(libs.kotlinx.serialization.json)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(projects.cx.foreignToolingCxCompilerTestSupport)
            }
        }
    }
}

// K/N configuration
tasks.withType<CInteropProcess>().configureEach {
    dependsOn(tasks.setupClangHeaders)
}

kotlin {
    targets.withType(KotlinNativeTargetWithHostTests::class).configureEach {
        val setupClangLibsTask = when (konanTarget) {
            KonanTarget.MACOS_ARM64 -> tasks.setupClangLibsMacosArm64
            KonanTarget.MACOS_X64   -> tasks.setupClangLibsMacosX64
            KonanTarget.LINUX_X64   -> tasks.setupClangLibsLinuxX64
            KonanTarget.MINGW_X64   -> tasks.setupClangLibsMingwX64
            else                    -> TODO("Not supported: $konanTarget")
        }
        binaries {
            sharedLib {
                baseName = "CxCompiler"
            }
        }
        compilations.named("main") {
            cinterops.create("declarations") {
                defFile("src/nativeMain/interop/clang.def")
                includeDirs(tasks.setupClangHeaders.map { it.destinationDir })
            }
        }
        compilations.configureEach {
            compilerOptions.configure {
                freeCompilerArgs.add("-linker-option")
                freeCompilerArgs.add(setupClangLibsTask.map { "-L${it.destinationDir}" })
            }
        }
        testRuns.configureEach {
            @Suppress("UNCHECKED_CAST")
            (this as ExecutionTaskHolder<KotlinNativeHostTest>).executionTask.configure {
                dependsOn(setupClangLibsTask)
                val envName = when (konanTarget.family) {
                    Family.OSX   -> "DYLD_LIBRARY_PATH"
                    Family.LINUX -> "LD_LIBRARY_PATH"
                    Family.MINGW -> "PATH"
                    else         -> TODO("Not supported: $konanTarget")
                }
                environment(envName, setupClangLibsTask.get().destinationDir)
            }
        }
    }
}

// K/JVM configuration

val linkCxCompilerLibMacosArm64 =
    kotlin.targets.withType(KotlinNativeTarget::class)
        .single { it.konanTarget == KonanTarget.MACOS_ARM64 }
        .binaries
//        .getSharedLib(NativeBuildType.DEBUG)
        .getSharedLib(NativeBuildType.RELEASE)
        .linkTaskProvider

val compileJvmJniMacosArm64 by tasks.registering(CompileJvmJni::class) {
    konanTargetName.set(KonanTarget.MACOS_ARM64.name)
    jdkHeadersDirectory.set(layout.dir(tasks.setupJdkHeadersMacosArm64.map { it.destinationDir }))

    linkLibraries.addAll("CxCompiler")

    // CxCompiler configuration
    dependsOn(linkCxCompilerLibMacosArm64)
    linkPaths.add(linkCxCompilerLibMacosArm64.flatMap { it.destinationDirectory })
    includePaths.add(linkCxCompilerLibMacosArm64.flatMap { it.destinationDirectory })

    // input C files and output library
    inputFiles.from(layout.projectDirectory.dir("src/jvmMain/c").asFileTree)
    outputFile.set(temporaryDir.resolve("libCxCompilerJni.dylib"))
}

val setupJvmResources by tasks.registering(Sync::class) {
    // TODO: add other OSs here
    from(tasks.setupClangLibsMacosArm64) {
        into("libs/macos-arm64")
    }
    from(compileJvmJniMacosArm64) {
        into("libs/macos-arm64")
    }
    from(linkCxCompilerLibMacosArm64.map { it.outputFile }) {
        into("libs/macos-arm64")
    }

    into(temporaryDir)
}

kotlin {
    sourceSets {
        jvmMain {
            resources.srcDir(setupJvmResources)
        }
    }
}
