import foreignbuild.*
import org.jetbrains.kotlin.gradle.*
import org.jetbrains.kotlin.gradle.plugin.*
import org.jetbrains.kotlin.gradle.targets.native.tasks.*
import org.jetbrains.kotlin.gradle.tasks.*
import org.jetbrains.kotlin.konan.target.*

plugins {
    id("foreignbuild.multiplatform-base")
    id("foreignbuild.setup-libclang")
    id("foreignbuild.setup-openssl")
}

tasks.withType<CInteropProcess>().configureEach {
    dependsOn(tasks.setupClangHeaders)
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    explicitApi()
    compilerOptions {
        optIn.add(OptIns.ExperimentalForeignApi)
    }

    nativeDesktopTargets {
        val setupClangLibsTask = when (konanTarget) {
            KonanTarget.MACOS_ARM64 -> tasks.setupClangLibsMacosArm64
            KonanTarget.MACOS_X64   -> tasks.setupClangLibsMacosX64
            KonanTarget.LINUX_X64   -> tasks.setupClangLibsLinuxX64
            KonanTarget.MINGW_X64   -> tasks.setupClangLibsMingwX64
            else                    -> TODO("Not supported: $konanTarget")
        }
        compilations.named("main") {
            cinterops.create("clang") {
                defFile("src/commonMain/interop/clang.def")
                includeDirs(tasks.setupClangHeaders.map { it.destinationDir })
            }
        }
        compilations.configureEach {
            compileTaskProvider {
                compilerOptions {
                    // TODO: this is needed to run
                    // TODO: recheck allocator
                    // standard allocator uses MUCH LESS memory
                    //freeCompilerArgs.add("-Xallocator=std")
                    // TODO: include those paths in `def` file to simplify handling
                    freeCompilerArgs.add("-linker-option")
                    freeCompilerArgs.add(setupClangLibsTask.map { "-L${it.destinationDir}" })
                }
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

    sourceSets {
        commonMain.dependencies {
            api(projects.foreignClangApi)
        }
        commonTest.dependencies {
            implementation(projects.foreignClangArguments)
            implementation(libs.kotlinx.io.core)
            implementation(kotlin("test"))
        }
    }
}
