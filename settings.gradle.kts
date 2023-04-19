enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    includeBuild("build-logic")
    includeBuild("build-parameters")
    includeBuild("build-kotlin")
}

plugins {
    id("kotlin-version-catalog")
    id("com.gradle.enterprise") version "3.12.2"
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.4.0")
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}

rootProject.name = "ffi-kotlin"

include("foreign-runtime:foreign-runtime-core")
include("foreign-runtime:foreign-runtime-c")

include("foreign-cx-index")
include("foreign-cx-index:foreign-cx-index-cli")
project(":foreign-cx-index:foreign-cx-index-cli").projectDir = file("foreign-cx-index/cli")

//include("foreign-gradle:foreign-gradle-jni")
//include("foreign-gradle:foreign-gradle-plugin")

fun includeLibrary(name: String) {
    listOf("api", "shared", "prebuilt", "test").forEach { submodule ->
        include("libraries:$name:$name-$submodule")
        project(":libraries:$name:$name-$submodule").projectDir = file("libraries/$name/$submodule")
    }
}

includeLibrary("libcrypto3")
