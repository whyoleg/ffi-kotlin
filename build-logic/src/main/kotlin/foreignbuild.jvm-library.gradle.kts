plugins {
    kotlin("jvm")
}

//@OptIn(ExperimentalKotlinGradlePluginApi::class)
//kotlin {
//    compilerOptions {
//        allWarningsAsErrors.set(true)
//        progressiveMode.set(true)
//        freeCompilerArgs.addAll(
//            "-Xrender-internal-diagnostic-names",
//            "-Xexpect-actual-classes"
//        )
//    }
//
//    targets.withType<KotlinJvmTarget>().configureEach {
//        compilations.configureEach {
//            compileTaskProvider {
//                compilerOptions {
//                    freeCompilerArgs.add("-Xjvm-default=all")
//                }
//            }
//        }
//    }
//
//    targets.withType<KotlinAndroidTarget>().configureEach {
//        compilations.configureEach {
//            compileTaskProvider {
//                compilerOptions {
//                    freeCompilerArgs.add("-Xjvm-default=all")
//                }
//            }
//        }
//    }
//
//    //setup tests running in RELEASE mode
//    targets.withType<KotlinNativeTarget>().configureEach {
//        binaries.test(listOf(NativeBuildType.RELEASE))
//    }
//    targets.withType<KotlinNativeTargetWithTests<*>>().configureEach {
//        testRuns.create("releaseTest") {
//            setExecutionSourceFrom(binaries.getTest(NativeBuildType.RELEASE))
//        }
//    }
//}
//
//// on build, link even those binaries, which it's not possible to run
//tasks.build {
//    dependsOn(tasks.withType<KotlinNativeLink>())
//}
