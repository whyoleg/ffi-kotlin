import com.android.build.gradle.tasks.*

plugins {
    id("buildx-multiplatform") //-library

    id("buildx-target-android")
    id("buildx-target-emscripten")
    id("buildx-target-native-all")
    id("buildx-target-jdk-all")

    id("buildx-use-android-jni")
    id("buildx-use-jvm-jni")
    id("buildx-use-openssl")
}

//tasks.withType<CInteropProcess>().configureEach {
//    dependsOn(openssl.prepareOpensslTaskProvider)
//}
//
tasks.named<jni.BuildJni>("buildJni") {
    linkLibraries.add("crypto")
    linkPaths.add(
        openssl.libDir("macos-arm64").map { it.absolutePath }
    )
    includeDirs.add(
        openssl.includeDir("macos-arm64").map { it.absolutePath }
    )
    outputFilePath.set("macos-arm64/libforeign-crypto-jni.dylib")
}

val copyLibrariesForAndroid by tasks.registering(Sync::class) {
    listOf(
        "android-arm64" to "arm64-v8a",
        "android-x64" to "x86_64",
    ).forEach { (opensslTarget, androidAbi) ->
        from(openssl.libDir(opensslTarget)) {
            into(androidAbi)
            include("libcrypto.so")
        }
    }
    into(layout.buildDirectory.dir("androidLibraries"))
}

val copyHeadersForAndroid by tasks.registering(Sync::class) {
    listOf(
        "android-arm64" to "arm64-v8a",
        "android-x64" to "x86_64",
    ).forEach { (opensslTarget, androidAbi) ->
        from(openssl.includeDir(opensslTarget)) {
            into("$androidAbi/include")
        }
    }
    into(layout.buildDirectory.dir("androidHeaders"))
}

tasks.withType<ExternalNativeBuildTask>().configureEach {
    dependsOn(copyHeadersForAndroid, copyLibrariesForAndroid)
}

tasks.maybeCreate("prepareKotlinIdeaImport").dependsOn(copyHeadersForAndroid, copyLibrariesForAndroid)

kotlin {
    sourceSets {
        all {
            languageSettings.optIn("dev.whyoleg.foreign.memory.ForeignMemoryApi")
        }
        commonMain {
            dependencies {
                api(projects.foreignRuntime.foreignRuntimeC)
            }
        }
    }
//    targets.all {
//        if (this is KotlinNativeTarget) {
//            val main by compilations.getting {
//                val declarations by cinterops.creating {
//                    defFile("src/nativeMain/interop/declarations.def")
//                    includeDirs(openssl.includeDir(konanTarget))
//                }
//            }
//        }
//    }
}
