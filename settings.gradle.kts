pluginManagement {
    includeBuild("build-settings")
}

plugins {
    id("foreignbuild.settings.default")
}

rootProject.name = "ffi-kotlin"

includeBuild("foreign-runtime")
includeBuild("foreign-tooling")
includeBuild("foreign-gradle-plugin")

includeBuild("test-projects")
