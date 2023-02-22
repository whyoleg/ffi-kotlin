import org.jetbrains.kotlin.gradle.targets.jvm.*

plugins {
    id("buildx-multiplatform-default")
    id("buildx-use-openssl")
}

kotlin {
    targets.all {
        if (this is KotlinJvmTarget) testRuns.all {
            executionTask.configure {
                dependsOn(openssl.prepareOpensslTaskProvider)
                environment("DYLD_LIBRARY_PATH", openssl.libDir("macos-arm64").get())
            }
        }
    }
    macosArm64("native") {
        val main by compilations.getting {
            val dynamic by cinterops.creating {
                defFile("main/native/interop/linking.def")
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
