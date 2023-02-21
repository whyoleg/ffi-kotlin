import org.jetbrains.kotlin.gradle.tasks.*

plugins {
    id("buildx-multiplatform-default")
}

val prepareOpenssl = rootProject.tasks.named("prepareOpenssl3", Sync::class)

fun opensslLib(target: String) = prepareOpenssl.map {
    it.destinationDir.resolve(target).resolve("lib")
}

tasks.withType<CInteropProcess>().configureEach {
    dependsOn(prepareOpenssl)
}

val copyLibrariesForJvm by tasks.registering(Sync::class) {
    dependsOn(prepareOpenssl)

    fun fromLibraries(target: String, extension: String, action: CopySpec.() -> Unit = {}) = from(opensslLib(target)) {
        into("libs/$target")
        include("*crypto*.$extension")
        action()
    }

    // `*3*` is needed to filter symlinks
    fromLibraries("macos-arm64", "dylib") { exclude("*3*") }
    fromLibraries("macos-x64", "dylib") { exclude("*3*") }
    fromLibraries("linux-x64", "so") { exclude("*3*") }
    fromLibraries("windows-x64", "dll")

    into(layout.buildDirectory.dir("jvmLibraries"))
}

kotlin {
    macosArm64("native") {
        val main by compilations.getting {
            val static by cinterops.creating {
                defFile("main/native/interop/linking.def")
                extraOpts("-libraryPath", opensslLib("macos-arm64").get())
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
        jvmMain {
            resources.srcDir(copyLibrariesForJvm.map { it.destinationDir })
        }
    }
}
