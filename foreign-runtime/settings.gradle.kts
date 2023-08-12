pluginManagement {
    includeBuild("../gradle/settings")
    includeBuild("../gradle/plugins")
}

plugins {
    id("default-settings")
}

rootProject.name = "foreign-runtime"

include("foreign-runtime-core")
include("foreign-runtime-c") // whole new API
