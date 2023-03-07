plugins {
    kotlin("multiplatform")
}

kotlin {
    explicitApi()
    macosArm64("native") {
        val main by compilations.getting {
            val declarations by cinterops.creating {
                defFile("src/nativeMain/interop/clang.def")
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api("com.squareup.okio:okio:3.3.0")
            }
        }
    }
}
