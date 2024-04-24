import foreignbuild.*
import org.jetbrains.kotlin.konan.target.*

plugins {
    id("foreignbuild.multiplatform-base")
    id("foreignbuild.setup-libclang")
}

kotlin {
    nativeDesktopTargets {
        val setupClangLibsTask = when (konanTarget) {
            KonanTarget.MACOS_ARM64 -> tasks.setupClangLibsMacosArm64
            KonanTarget.MACOS_X64   -> tasks.setupClangLibsMacosX64
            KonanTarget.LINUX_X64   -> tasks.setupClangLibsLinuxX64
            KonanTarget.MINGW_X64   -> tasks.setupClangLibsMingwX64
            else                    -> TODO("Not supported: $konanTarget")
        }
        binaries {
            executable {
                entryPoint("dev.whyoleg.foreign.clang.cli.main")
                runTaskProvider!! {
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
        compilations.configureEach {
            compileTaskProvider {
                compilerOptions {
                    freeCompilerArgs.add("-linker-option")
                    freeCompilerArgs.add(setupClangLibsTask.map { "-L${it.destinationDir}" })
                }
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.foreignClangCliCommands)
            implementation(projects.foreignClangCompiler)
            implementation(libs.kotlinx.io.core)
        }
    }
}
