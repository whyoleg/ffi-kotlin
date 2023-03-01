import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.gradle.targets.jvm.*

plugins {
    id("buildx-multiplatform-library")

    id("buildx-target-native-desktop")
    id("buildx-target-jvm-all")

    id("buildx-use-openssl")
}

kotlin {
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
    targets.all {
        if (this is KotlinJvmTarget) testRuns.all {
            executionTask.configure {
                dependsOn(openssl.prepareOpensslTaskProvider)
                environment("DYLD_LIBRARY_PATH", openssl.libDir("macos-arm64").get())
            }
        } else if (this is KotlinNativeTarget) {
            val main by compilations.getting {
                val shared by cinterops.creating {
                    defFile("src/nativeMain/interop/linking.def")
                }
            }
        }
    }
}
