pluginManagement {
    includeBuild("../gradle/settings")
    includeBuild("../gradle/plugins")
}

plugins {
    id("default-settings")
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "foreign-gradle-plugin"

includeBuild("../foreign-tools")
