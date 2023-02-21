import org.jetbrains.kotlin.gradle.tasks.*

plugins {
    id("buildx-multiplatform-default")
}

val prepareOpenssl = rootProject.tasks.named("prepareOpenssl3", Sync::class)

fun opensslInclude(target: String) = prepareOpenssl.map {
    it.destinationDir.resolve(target).resolve("include")
}

tasks.withType<CInteropProcess>().configureEach {
    dependsOn(prepareOpenssl)
}

kotlin {
    macosArm64("native") {
        val main by compilations.getting {
            val declarations by cinterops.creating {
                defFile("main/native/interop/declarations.def")
                includeDirs(opensslInclude("macos-arm64"))
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
