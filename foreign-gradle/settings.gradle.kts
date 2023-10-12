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

include("foreign-gradle-plugin")
include("foreign-gradle-plugin-api")
include("foreign-gradle-internal-tool")

includeBuild("../foreign-tooling")
