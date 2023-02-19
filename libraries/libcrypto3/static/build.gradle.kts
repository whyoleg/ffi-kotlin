plugins {
    id("buildx-multiplatform-default")
}

kotlin {
    macosArm64("native") {
        val main by compilations.getting {
            val static by cinterops.creating {
                defFile("linking.def")
                extraOpts("-libraryPath", rootDir.resolve("prebuilt/openssl3/macos-arm64/lib/static").absolutePath)
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
