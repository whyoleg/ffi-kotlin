import org.jetbrains.kotlin.gradle.tasks.*
import wasm.*

plugins {
    id("buildx-multiplatform-default")
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

kotlin {
    macosArm64("native") {
        val main by compilations.getting {
            val static by cinterops.creating {
                defFile("src/nativeMain/interop/linking.def")
                extraOpts("-libraryPath", openssl.libDir("macos-arm64").get())
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

kotlin {
    js {
        nodejs()
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

    sourceSets {
        val jsMain by getting {
            dependencies {
                api(generateWasmNpmModule.map {
                    npm(libname, it.outputDirectory.get().asFile)
                })
            }
        }
    }
}

//TODO: hack to resolve dependencies
tasks.maybeCreate("prepareKotlinIdeaImport").dependsOn(generateWasmNpmModule)
tasks.named("compileKotlinJs") { dependsOn(generateWasmNpmModule) }
