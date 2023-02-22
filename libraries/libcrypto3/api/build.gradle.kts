import org.jetbrains.kotlin.gradle.tasks.*

plugins {
    id("buildx-multiplatform-default")
    id("buildx-use-openssl")
}

tasks.withType<CInteropProcess>().configureEach {
    dependsOn(openssl.prepareOpensslTaskProvider)
}

kotlin {
    macosArm64("native") {
        val main by compilations.getting {
            val declarations by cinterops.creating {
                defFile("main/native/interop/declarations.def")
                includeDirs(openssl.includeDir("macos-arm64"))
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
