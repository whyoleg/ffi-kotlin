enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    includeBuild("gradle/settings")
    includeBuild("gradle/plugins")
    includeBuild("gradle/parameters")

    includeBuild("foreign-gradle-plugin")
}

plugins {
    id("kotlin-version-catalog")
    id("com.gradle.enterprise") version "3.12.2"
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.5.0")
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

rootProject.name = "foreign-kotlin"

include("foreign-runtime:foreign-runtime-core")
include("foreign-runtime:foreign-runtime-c") // whole new API

//test libraries

fun includeLibrary(name: String) {
    listOf("api", "shared", "prebuilt", "test").forEach { submodule ->
        include("libraries:$name:$name-$submodule")
        project(":libraries:$name:$name-$submodule").projectDir = file("libraries/$name/$submodule")
    }
}

//includeLibrary("libcrypto3")
