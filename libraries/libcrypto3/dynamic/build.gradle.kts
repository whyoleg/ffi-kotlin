import org.jetbrains.kotlin.gradle.targets.jvm.*

plugins {
    id("buildx-multiplatform-default")
}

val prepareOpenssl = rootProject.tasks.named("prepareOpenssl3", Sync::class)

fun opensslLib(target: String) = prepareOpenssl.map {
    it.destinationDir.resolve(target).resolve("lib")
}

kotlin {
    targets.all {
        if (this is KotlinJvmTarget) testRuns.all {
            executionTask.configure {
                dependsOn(prepareOpenssl)
                environment("DYLD_LIBRARY_PATH", opensslLib("macos-arm64").get())
            }
        }
    }
    macosArm64("native") {
        val main by compilations.getting {
            val dynamic by cinterops.creating {
                defFile("linking.def")
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.libraries.libcrypto3.libcrypto3Api)
            }
        }
        commonTest {
            dependencies {
                api(projects.libraries.libcrypto3.libcrypto3Test)
            }
        }
    }
}
