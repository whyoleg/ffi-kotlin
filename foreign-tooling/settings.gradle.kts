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
    "model",
    "indexer",
    "compiler",
    "bridge-generator",
    "playground" // TODO: drop it, and somehow write tests
).forEach {
    include("cx:$it")
    project(":cx:$it").apply {
        projectDir = file("cx/$it")
        name = "foreign-tooling-cx-$it"
    }
}
