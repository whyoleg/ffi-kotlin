import org.jetbrains.kotlin.gradle.tasks.*

plugins {
    id("buildx-multiplatform-default")
    id("buildx-use-openssl")
}

tasks.withType<CInteropProcess>().configureEach {
    dependsOn(openssl.prepareOpensslTaskProvider)
}

tasks.named<jni.BuildJni>("buildJni") {
    linkLibraries.add("crypto")
    linkPaths.addAll(
        "/opt/homebrew/opt/openssl@3/lib",
        "/usr/local/opt/openssl@3/lib",
    )
    includeDirs.add(
        openssl.includeDir("macos-arm64").map { it.absolutePath }
    )
    outputFilePath.set("macos-arm64/libcrypto-jni.dylib")
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

kotlin {
    wasm {
        nodejs()
    }
    js {
        nodejs()
    }
}
