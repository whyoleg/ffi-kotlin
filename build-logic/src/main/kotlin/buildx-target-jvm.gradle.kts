import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    id("buildx-multiplatform")
}

kotlin {
    //setup JDK 20 toolchain for FFM compilation
    jvmToolchain(20)

    jvm("jvmJdk") {
        compilations.all {
            compilerOptions.configure {
                jvmTarget.set(JvmTarget.JVM_1_8)
            }
        }
        testRuns {
            create("8") {
                executionTask.configure {
                    javaLauncher.set(javaToolchains.launcherFor {
                        languageVersion.set(JavaLanguageVersion.of(8))
                    })
                }
            }
        }
    }
}
