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
rootProject.name = "foreign-runtime"

include("foreign-runtime-core")
include("foreign-runtime-c") // whole new API
//include("foreign-runtime-kotlinx.cinterop")
