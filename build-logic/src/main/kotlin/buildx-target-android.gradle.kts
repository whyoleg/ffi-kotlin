import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    id("com.android.library")
    id("buildx-multiplatform")
}

android {
    //TODO drop static/X
    namespace = "${project.group}.${project.name.replace("-", ".")}X"
    compileSdk = 33
    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

kotlin {
    android {
        compilations.all {
            compilerOptions.configure {
                jvmTarget.set(JvmTarget.JVM_1_8)
            }
        }
    }
    afterEvaluate {
        this@kotlin.sourceSets {
            val androidInstrumentedTest by getting {
                dependsOn(commonTest.get())
                dependencies {
                    implementation("androidx.test:runner:1.5.2")
                }
            }
        }
    }
}
