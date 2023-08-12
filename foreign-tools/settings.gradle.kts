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

rootProject.name = "foreign-tools"

include("indexes:foreign-index-cx")
include("indexes:foreign-index-cx-cli")
include("schemas:foreign-schema-cx")
include("generators:foreign-generator-cx")
