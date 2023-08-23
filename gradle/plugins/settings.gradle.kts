pluginManagement {
    includeBuild("../../build-settings")
}

plugins {
    id("default-settings")
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../libs.versions.toml"))
        }
    }
}

rootProject.name = "build-plugins"

includeBuild("../../build-parameters")
