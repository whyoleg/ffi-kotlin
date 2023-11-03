pluginManagement {
    includeBuild("../build-settings")
}

plugins {
    id("foreignbuild.settings.default")
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "foreign-gradle"

includeBuild("../foreign-tooling")

include("foreign-gradle-plugin")
include("foreign-gradle-internal-tool")

// this is a candidate for usage in Gradle plugin, Kotlin compiler plugin API, IDEA plugin, etc
include("foreign-gradle-tooling")
