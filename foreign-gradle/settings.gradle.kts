pluginManagement {
    includeBuild("../gradle/settings")
    includeBuild("../gradle/plugins")
}

plugins {
    id("default-settings")
}

rootProject.name = "foreign-gradle"

include("foreign-gradle-plugin")

includeBuild("../foreign-tools") {
    dependencySubstitution {
        substitute(module("dev.whyoleg.foreign:foreign-generator-cx")).using(project(":generators:foreign-generator-cx"))
    }
}
