import com.android.build.gradle.tasks.*
import org.jetbrains.kotlin.gradle.plugin.*
import org.jetbrains.kotlin.gradle.targets.js.dsl.*
import wasm.*

plugins {
    id("buildx-multiplatform-library")

    id("buildx-target-android")
    id("buildx-target-emscripten")
    id("buildx-target-native-all")
    id("buildx-target-jdk-all")

    id("buildx-use-openssl")
}

//tasks.withType<CInteropProcess>().configureEach {
//    dependsOn(openssl.prepareOpensslTaskProvider)
//}

val copyLibrariesForJvm by tasks.registering(Sync::class) {
    fun fromLibraries(target: String, extension: String, action: CopySpec.() -> Unit = {}) = from(openssl.libDir(target)) {
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

val libname = "foreign-crypto-wasm"

val linkWasm by tasks.registering(DefaultLinkWasm::class) {
    linkLibraries.addAll("crypto", "z")
    linkPaths.add(
        openssl.libDir("wasm").map { it.absolutePath }
    )
    includeDirs.add(
        openssl.includeDir("wasm").map { it.absolutePath }
    )
    outputLibraryName.set(libname)
}

val generateWasmTestRunner by tasks.registering(DefaultGenerateWasmTestRunner::class) {
    dependsOn("emscriptenWasmTestTestDevelopmentExecutableCompileSync")//TODO
    dependsOn(linkWasm)
    inputLibraryName.set(libname)
    inputLibraryFile.set(linkWasm.flatMap { it.producedLibraryFile })
}

val generateWasmNpmModule = tasks.registerGenerateWasmNpmModuleTask {
    dependsOn(linkWasm)
    inputLibraryDirectory.set(linkWasm.flatMap { it.outputDirectory })
    inputLibraryName.set(linkWasm.flatMap { it.producedLibraryFile }.map { it.asFile.name })
}

tasks.withType<MergeSourceSetFolders>().configureEach {
    //TODO: should only depend for JNI folder
    dependsOn(copyLibrariesForAndroid)
}

android {
    val main by sourceSets.getting {
        jniLibs.srcDir(copyLibrariesForAndroid.map { it.destinationDir })
    }
}

kotlin {
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
        emscriptenJsMain {
            dependencies {
                api(generateWasmNpmModule.map {
                    npm(libname, it.outputDirectory.get().asFile)
                })
            }
        }
    }
    targets.all {
//        if (this is KotlinNativeTarget) {
//            val main by compilations.getting {
//                val prebuilt by cinterops.creating {
//                    defFile("src/nativeMain/interop/linking.def")
//                    extraOpts("-libraryPath", openssl.libDir(konanTarget).get())
//                }
//            }
//        }
        if (this is KotlinWasmTargetDsl && platformType == KotlinPlatformType.wasm) {
            nodejs {
                testTask {
                    dependsOn(generateWasmTestRunner)
                    val originalPath = inputFileProperty.get().asFile.absolutePath.replace(".mjs", ".uninstantiated.mjs")
                    //TODO: what to do here???
                    generateWasmTestRunner.get().instantiateFile.set(originalPath)
                    inputFileProperty.set(generateWasmTestRunner.flatMap { it.testRunnerFile })
                }
            }
        }
    }
}

//TODO: hack to resolve dependencies
tasks.maybeCreate("prepareKotlinIdeaImport").dependsOn(generateWasmNpmModule)
tasks.named("compileKotlinEmscriptenJs") { dependsOn(generateWasmNpmModule) }
