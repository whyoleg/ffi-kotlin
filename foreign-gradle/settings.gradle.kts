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
include("foreign-gradle-plugin-api")
include("foreign-gradle-internal-tool")
