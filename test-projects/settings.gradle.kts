pluginManagement {
    includeBuild("../build-settings")

    includeBuild("../build-logic")
}

plugins {
    id("foreignbuild.settings.default")
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "test-projects"

//includeBuild("../foreign-runtime")
includeBuild("../foreign-gradle")

includeTestProject("libcrypto", "api", "prebuilt", "shared", "test")

fun includeTestProject(name: String, vararg submodules: String) {
    submodules.forEach { submodule ->
        include("$name:$name-$submodule")
        project(":$name:$name-$submodule").projectDir = file("$name/$submodule")
    }
}
