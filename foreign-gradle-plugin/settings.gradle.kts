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

includeBuild("../foreign-tooling")

