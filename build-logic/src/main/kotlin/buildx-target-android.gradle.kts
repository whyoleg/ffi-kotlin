import com.android.build.api.dsl.*
import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    id("com.android.library")
    id("buildx-multiplatform")
}

android {
    namespace = "${project.group}.${project.name.replace("-", ".")}"
    compileSdk = 33
    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    @Suppress("UnstableApiUsage")
    testOptions {
        managedDevices {
            devices {
                maybeCreate<ManagedVirtualDevice>("pixel2_33").apply {
                    device = "Pixel 2"
                    apiLevel = 33
                }
            }
        }
    }
}

kotlin {
    android("jvmAndroidJni") {
        compilations.all {
            compilerOptions.configure {
                jvmTarget.set(JvmTarget.JVM_1_8)
            }
        }
    }
    afterEvaluate {
        this@kotlin.sourceSets {
            val jvmAndroidJniInstrumentedTest by getting {
                dependsOn(commonTest.get())
                dependencies {
                    implementation("androidx.test:runner:1.5.2")
                }
            }
        }
    }
}
