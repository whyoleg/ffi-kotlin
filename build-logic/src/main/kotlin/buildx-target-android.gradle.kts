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
            val androidInstrumentedTest by getting {
                dependsOn(commonTest.get())
                dependencies {
                    implementation("androidx.test:runner:1.5.2")
                }
            }
        }
    }
}
