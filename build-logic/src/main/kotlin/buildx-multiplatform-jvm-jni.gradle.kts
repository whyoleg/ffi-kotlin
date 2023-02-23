import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    id("buildx-multiplatform")
}

val buildJni by tasks.registering(jni.DefaultBuildJni::class)

kotlin {
    jvm("jvmJni") {
        attributes.attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 8)
        compilations.all {
            compilerOptions.configure {
                jvmTarget.set(JvmTarget.JVM_1_8)
                freeCompilerArgs.add("-Xjdk-release=1.8")
            }
        }
        testRuns.all {
            executionTask.configure {
                javaLauncher.set(javaToolchains.launcherFor {
                    languageVersion.set(JavaLanguageVersion.of(8))
                })
            }
        }
    }
    sourceSets {
        val jvmJniMain by getting {
            resources.srcDir(buildJni.map { it.outputDirectory })
        }
    }
}
