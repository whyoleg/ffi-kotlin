import org.jetbrains.kotlin.gradle.*
import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    id("foreignbuild.multiplatform-library")
    id("foreignbuild.multiplatform-all-targets")
    id("foreignbuild.multiplatform-android")
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    jvmToolchain {
        languageVersion.set(libs.versions.java.ffm.map(JavaLanguageVersion::of))
    }
    jvm {
        compilations.configureEach {
            compilerOptions.configure {
                jvmTarget.set(JvmTarget.JVM_1_8)
            }
        }
        testRuns.create("8") {
            executionTask.configure {
                javaLauncher.set(javaToolchains.launcherFor {
                    languageVersion.set(JavaLanguageVersion.of(8))
                })
            }
        }
    }
    targetHierarchy.custom {
        common {
            group("platformInt") {
                // shares common wasm api
                group("emscripten") {
                    withJs()
                    withWasm()
                }
            }
            group("platformLong") {
                group("native") {
                    withNative()
                }
                // shares a library loading mechanism and some of the platform specifics
                group("jdk") {
                    withAndroidTarget()
                    withJvm()
                }
            }
        }
    }
}
