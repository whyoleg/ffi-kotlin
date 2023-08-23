import com.android.build.api.dsl.*
import org.jetbrains.kotlin.gradle.*
import org.jetbrains.kotlin.gradle.plugin.*

plugins {
    id("com.android.library")
    kotlin("multiplatform")
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    androidTarget {
        instrumentedTestVariant.sourceSetTree.set(KotlinTargetHierarchy.SourceSetTree.test)
        unitTestVariant.sourceSetTree.set(KotlinTargetHierarchy.SourceSetTree.unitTest)
    }
    sourceSets {
        invokeWhenCreated("androidInstrumentedTest") {
            dependencies {
                implementation("androidx.test:runner:1.5.2")
            }
        }
    }
}

android {
    namespace = "${project.group}.${project.name.replace("-", ".")}"
    compileSdk = 33
    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // setup for local dev, on CI same targets are tested but with different approach
    @Suppress("UnstableApiUsage")
    testOptions {
        managedDevices {
            devices {
                maybeCreate<ManagedVirtualDevice>("androidApi21").apply {
                    device = "Pixel 2"
                    apiLevel = 21
                    systemImageSource = "aosp"
                }
                maybeCreate<ManagedVirtualDevice>("androidApi30").apply {
                    device = "Pixel 2"
                    apiLevel = 30
                    systemImageSource = "aosp-atd"
                }
                maybeCreate<ManagedVirtualDevice>("androidApi33").apply {
                    device = "Pixel 2"
                    apiLevel = 33
                    systemImageSource = "aosp-atd"
                }
            }
            groups {
                maybeCreate("androidAll").apply {
                    targetDevices.addAll(devices)
                }
            }
        }
    }
}
