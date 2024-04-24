import foreignbuild.*
import org.jetbrains.kotlin.gradle.*
import org.jetbrains.kotlin.gradle.dsl.*
import org.jetbrains.kotlin.gradle.plugin.*

plugins {
    id("foreignbuild.multiplatform-library")
    id("foreignbuild.setup-jdk")
    alias(libs.plugins.android.library)
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    jvmTarget(22) {
        compilations.configureEach {
            compileTaskProvider {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8)
                }
            }
        }
    }

    androidTarget {
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
        unitTestVariant.sourceSetTree.set(KotlinSourceSetTree.unitTest)

        compilations.configureEach {
            compileTaskProvider {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8)
                }
            }
        }
    }

    nativeTargets()
    webTargets()

    // TODO: hwo to support wasmWasi ?

    sourceSets.invokeWhenCreated("androidInstrumentedTest") {
        dependencies {
            implementation(libs.androidx.test)
        }
    }

    applyHierarchyTemplate {
        common {
            // shares common wasm api
            group("web") {
                group("webJs") {
                    withJs()
                }
                group("webWasmJs") {
                    withWasmJs()
                }
            }

            // has access to global memory
            group("global") {
                // shares a library loading mechanism and some of the platform specifics
                group("jdk") {
                    group("jdkAndroid") {
                        withAndroidTarget()
                    }
                    group("jdkJvm") {
                        withJvm()
                    }
                }

                group("native") {
                    group("nativeInt") {
                        withWatchosArm32()
                        withWatchosArm64()
                    }
                    group("nativeLong") {
                        withWatchosX64()
                        withWatchosSimulatorArm64()
                        withWatchosDeviceArm64()

                        // we support only 2 Linux targets (x64 and arm64)
                        withLinux()
                        // we support only 1 mingw target (x64)
                        withMingw()

                        withMacos()
                        withIos()
                        withTvos()

                        // TODO!!!
                        withAndroidNative()
                    }
                }
            }
        }
    }
}

android {
    namespace = "dev.whyoleg.foreign"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // ndk.abiFilters += listOf("x86_64", "arm64-v8a")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    externalNativeBuild {
        ndkBuild {
            path("src/jdkAndroidMain/jni/Android.mk")
        }
    }
}

// TODO: setup JVM JNI

//val compileJvmJniMacosArm64 by tasks.registering(CompileJvmJni::class) {
//    konanTargetName.set(KonanTarget.MACOS_ARM64.name)
//    jdkHeadersDirectory.set(layout.dir(tasks.setupJdkHeadersMacosArm64.map { it.destinationDir }))
//
//    // CxCompiler configuration
//    linkPaths.add(linkCxCompilerLibMacosArm64.flatMap { it.destinationDirectory })
//    includePaths.add(linkCxCompilerLibMacosArm64.flatMap { it.destinationDirectory })
//
//    // input C files and output library
//    inputFiles.from(layout.projectDirectory.dir("src/jvmMain/c").asFileTree)
//    outputFile.set(temporaryDir.resolve("libCxCompilerJni.dylib"))
//}
//
//val setupJvmResources by tasks.registering(Sync::class) {
//    // TODO: add other OSs here
//    from(tasks.setupClangLibsMacosArm64) {
//        into("libs/macos-arm64")
//    }
//    from(compileJvmJniMacosArm64) {
//        into("libs/macos-arm64")
//    }
//    from(linkCxCompilerLibMacosArm64.map { it.outputFile }) {
//        into("libs/macos-arm64")
//    }
//
//    into(temporaryDir)
//}
//
//kotlin {
//    sourceSets {
//        jvmMain {
//            resources.srcDir(setupJvmResources)
//        }
//    }
//}
