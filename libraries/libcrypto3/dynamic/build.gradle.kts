plugins {
    id("buildx-multiplatform-default")
}

kotlin {
    jvm("jvmPanama") {
        testRuns.all {
            executionTask.configure {
                environment(
                    "DYLD_LIBRARY_PATH",
                    rootDir.resolve("prebuilt/openssl3/macos-arm64/lib/dynamic").absolutePath
                )
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
