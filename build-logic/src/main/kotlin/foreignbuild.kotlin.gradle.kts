import org.jetbrains.kotlin.gradle.*
import org.jetbrains.kotlin.gradle.dsl.*
import org.jetbrains.kotlin.gradle.plugin.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.gradle.targets.jvm.*
import org.jetbrains.kotlin.gradle.tasks.*

plugins {
    base
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
plugins.withType<KotlinBasePluginWrapper>().configureEach {
    extensions.configure<KotlinProjectExtension>("kotlin") {
        explicitApi()

        // true by default
        val warningsAsErrors = providers.gradleProperty("foreignbuild.warningsAsErrors").orNull?.toBoolean() ?: true

        if (providers.gradleProperty("foreignbuild.skipTests").map(String::toBoolean).getOrElse(false)) {
            tasks.matching { it is AbstractTestTask }.configureEach { onlyIf { false } }
        }

        fun KotlinCommonCompilerOptions.configureCommonOptions() {
            allWarningsAsErrors.set(warningsAsErrors)
            progressiveMode.set(true)
            freeCompilerArgs.addAll(
                "-Xrender-internal-diagnostic-names",
                "-Xexpect-actual-classes"
            )
        }

        fun KotlinJvmCompilerOptions.configureJvmOptions() {
            freeCompilerArgs.add("-Xjvm-default=all")
        }

        when (this) {
            is KotlinJvmProjectExtension     -> {
                compilerOptions.configureCommonOptions()
                target {
                    compilerOptions.configureJvmOptions()
                }
            }

            is KotlinAndroidProjectExtension -> {
                compilerOptions.configureCommonOptions()
                target {
                    compilerOptions.configureJvmOptions()
                }
            }

            is KotlinMultiplatformExtension  -> {
                compilerOptions.configureCommonOptions()
                targets.withType<KotlinJvmTarget>().configureEach {
                    compilerOptions.configureJvmOptions()
                }
                targets.withType<KotlinAndroidTarget>().configureEach {
                    compilerOptions.configureJvmOptions()
                }

                // setup tests running in RELEASE mode
                targets.withType<KotlinNativeTarget>().configureEach {
                    binaries.test(listOf(NativeBuildType.RELEASE))
                }
                targets.withType<KotlinNativeTargetWithTests<*>>().configureEach {
                    testRuns.create("releaseTest") {
                        setExecutionSourceFrom(binaries.getTest(NativeBuildType.RELEASE))
                    }
                }
                // on build, link even those binaries, which it's not possible to run
                tasks.build {
                    dependsOn(tasks.withType<KotlinNativeLink>())
                }
            }
        }
    }
}
