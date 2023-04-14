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

// Code is divided in separate modules depending on the layer of foreign access
include("foreign-core") // foreign memory management, library loading (for some targets), base FFI (for some targets), PlatformInt
include("foreign-runtime-c") // C types
include("foreign-c-playground-lib") // some library declarations using C API
include("foreign-c-playground") // some user code

//foreign-c-runtime
//foreign-c-index (mpp, serialization)
//foreign-c-indexer (K/N cli, uses cinterop)
//foreign-c-generator (generates everything, that's needed to build foreign code based on index)
//foreign-c-compiler-plugin

//foreign-gradle-plugin
//foreign-idea-plugin - tbd what will be here :)

//foreign-conan

fun includeLibrary(name: String) {
    listOf("api"/*, "shared", "prebuilt", "test"*/).forEach { submodule ->
        include("libraries:$name:$name-$submodule")
        project(":libraries:$name:$name-$submodule").projectDir = file("libraries/$name/$submodule")
    }
}

includeLibrary("libcrypto3")
