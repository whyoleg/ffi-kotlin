import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.gradle.tasks.*
import org.jetbrains.kotlin.konan.target.*

plugins {
    id("buildx-multiplatform") //-library

    id("buildx-target-web")
    id("buildx-target-native-all")
    id("buildx-target-jvm-all")

    id("buildx-use-jvm-jni")
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
    sourceSets {
        commonMain {
            dependencies {
                api(projects.ffiRuntime)
            }
        }
    }
    targets.all {
        if (this is KotlinNativeTarget) {
            check(this.konanTarget == KonanTarget.MACOS_ARM64)
            val main by compilations.getting {
                val declarations by cinterops.creating {
                    defFile("src/nativeMain/interop/declarations.def")
                    includeDirs(openssl.includeDir("macos-arm64"))
                }
            }
        }
    }
}
