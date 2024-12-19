pluginManagement {
    includeBuild("build-settings")
}

plugins {
    id("foreignsettings.default")
}

rootProject.name = "ffi-kotlin"

includeBuild("foreign-kotlin")
includeBuild("samples")
