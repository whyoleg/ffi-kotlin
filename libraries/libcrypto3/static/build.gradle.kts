import com.android.build.gradle.tasks.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.gradle.tasks.*
import org.jetbrains.kotlin.konan.target.*
import wasm.*

plugins {
    id("buildx-multiplatform-library")

    id("buildx-target-android")
    id("buildx-target-web")
    id("buildx-target-native-all")
    id("buildx-target-jvm-all")

    id("buildx-use-openssl")
}

tasks.withType<CInteropProcess>().configureEach {
    dependsOn(openssl.prepareOpensslTaskProvider)
}

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

val libname = "ffi-libcrypto"

val linkWasm by tasks.registering(wasm.DefaultLinkWasm::class) {
    linkLibraries.addAll("crypto", "z")
    linkPaths.add(
        openssl.libDir("wasm").map { it.absolutePath }
    )
    includeDirs.add(
        openssl.includeDir("wasm").map { it.absolutePath }
    )
    outputLibraryName.set(libname)
}

val generateWasmTestRunner by tasks.registering(wasm.DefaultGenerateWasmTestRunner::class) {
    dependsOn("wasmTestTestDevelopmentExecutableCompileSync")//TODO
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
        jsMain {
            dependencies {
                api(generateWasmNpmModule.map {
                    npm(libname, it.outputDirectory.get().asFile)
                })
            }
        }
    }
    targets.all {
        if (this is KotlinNativeTarget) {
            check(this.konanTarget == KonanTarget.MACOS_ARM64)
            val main by compilations.getting {
                val static by cinterops.creating {
                    defFile("src/nativeMain/interop/linking.def")
                    extraOpts("-libraryPath", openssl.libDir("macos-arm64").get())
                }
            }
        }
    }
    wasm {
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

//TODO: hack to resolve dependencies
tasks.maybeCreate("prepareKotlinIdeaImport").dependsOn(generateWasmNpmModule)
tasks.named("compileKotlinJs") { dependsOn(generateWasmNpmModule) }
