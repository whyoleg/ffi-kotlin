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

rootProject.name = "foreign-gradle-plugin"

// TODO: split plugin into API and impl
//include("foreign-gradle-plugin")
//include("foreign-gradle-plugin-api")

includeBuild("../foreign-tooling")

