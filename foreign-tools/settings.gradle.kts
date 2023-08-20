pluginManagement {
    includeBuild("../gradle/settings")
    includeBuild("../gradle/plugins")
}

plugins {
    id("default-settings")
}

rootProject.name = "foreign-tools"

include("indexes:foreign-index-cx")
include("indexes:foreign-index-cx-generator")
include("schemas:foreign-schema-cx")
include("generators:foreign-generator-cx")
