pluginManagement {
    includeBuild("../gradle/settings")
    includeBuild("../gradle/plugins")

    includeBuild("build-logic")
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

// index of C (or C++ in future) declarations parsed from headers
include("foreign-cx-index")
// uses libclang to generate index - works for both JVM and K/N
include("foreign-cx-index-generator")
// metadata of declarations which will be stored in jar/klib (or separate artifact) to allow cross-references
include("foreign-cx-metadata")
// generates source code from index
include("foreign-cx-bindings-generator")
