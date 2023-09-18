pluginManagement {
    includeBuild("../build-settings")

    includeBuild("../build-logic")
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

rootProject.name = "foreign-tooling"

listOf(
    "compiler",
    "compiler-model",
    "compiler-runner",
    "bridge-model",
    "bridge-aggregator",
    "bridge-code-generator"
).forEach {
    include("cx:$it")
    project(":cx:$it").apply {
        projectDir = file("cx/$it")
        name = "foreign-tooling-cx-$it"
    }
}
