enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    includeBuild("../gradle/settings")
    includeBuild("../gradle/plugins")
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
        gradlePluginPortal()
    }
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}

rootProject.name = "foreign-tools"

include("indexes:foreign-index-cx")
include("indexes:foreign-index-cx-cli")
include("schemas:foreign-schema-cx")
include("generators:foreign-generator-cx")
