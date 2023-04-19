import org.jetbrains.kotlin.gradle.plugin.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.gradle.targets.jvm.*
import org.jetbrains.kotlin.gradle.targets.native.tasks.*

plugins {
    id("buildx-multiplatform-library")

    id("buildx-target-native-desktop")
    id("buildx-target-jdk-all")

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
            //TODO: decide on cinterop vs plain compiler args
//            val main by compilations.getting {
//                val shared by cinterops.creating {
//                    defFile("src/nativeMain/interop/linking.def")
//                }
//            }
            val test by compilations.getting {
                compilerOptions.configure {
                    freeCompilerArgs.addAll("-linker-option", "-lcrypto")
                    freeCompilerArgs.add("-linker-option")
                    freeCompilerArgs.add(openssl.libDir("macos-arm64").map { "-L${it.absolutePath}" })
                }
            }

            if (this is KotlinNativeTargetWithTests<*>) testRuns.all {
                (this as ExecutionTaskHolder<KotlinNativeTest>).executionTask.configure {
                    dependsOn(openssl.prepareOpensslTaskProvider)
                    environment("DYLD_LIBRARY_PATH", openssl.libDir("macos-arm64").get())
                }
            }
        }
    }
}
