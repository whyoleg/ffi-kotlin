plugins {
    id("buildx-multiplatform-default")
}

val copyLibrariesForJvm by tasks.registering(Sync::class) {
    from(rootDir.resolve("prebuilt/openssl3")) {
        include("*/lib/dynamic/*crypto*")
        //TODO: is it ok to filter symlinks in such way?
        exclude("**/*3*")
    }
    into(layout.buildDirectory.dir("jvmLibraries"))
    eachFile {
        path = "libs/${path.substringBefore("/")}/$name"
    }
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
        jvmMain {
            resources.srcDir(copyLibrariesForJvm.map { it.destinationDir })
        }
    }
}
