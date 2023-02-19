plugins {
    id("buildx-multiplatform-default")
}

kotlin {
    macosArm64("native") {
        val main by compilations.getting {
            val declarations by cinterops.creating {
                defFile("declarations.def")
                includeDirs(rootDir.resolve("prebuilt/openssl3/macos-arm64/include"))
            }
        }
    }
    sourceSets {
        commonMain {
            dependencies {
                api(projects.ffiRuntime)
            }
        }
    }
}
