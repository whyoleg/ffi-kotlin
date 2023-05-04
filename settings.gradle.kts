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
//include("foreign-runtime:foreign-runtime-kotlinx.cinterop") // kotlinx.cinterop compatible API

//TODO: move to composite build, or it will be not possible to test it normally
//includeBuild("foreign-tools")

include("foreign-tools:indexes:foreign-index-cx")
include("foreign-tools:indexes:foreign-index-cx-cli")

include("foreign-tools:schemas:foreign-schema-cx")

include("foreign-tools:generators:foreign-generator-cx")

//TODO: move to composite build
include("foreign-gradle-plugin")


//test libraries

fun includeLibrary(name: String) {
    listOf("api", "shared", "prebuilt", "test").forEach { submodule ->
        include("libraries:$name:$name-$submodule")
        project(":libraries:$name:$name-$submodule").projectDir = file("libraries/$name/$submodule")
    }
}

//includeLibrary("libcrypto3")
