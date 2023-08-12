plugins {
    id("com.gradle.enterprise") version "3.12.2"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}

rootProject.name = "foreign-kotlin"

includeBuild("foreign-runtime")
includeBuild("foreign-gradle-plugin")

//test libraries

fun includeLibrary(name: String) {
    listOf("api", "shared", "prebuilt", "test").forEach { submodule ->
        include("libraries:$name:$name-$submodule")
        project(":libraries:$name:$name-$submodule").projectDir = file("libraries/$name/$submodule")
    }
}

//includeLibrary("libcrypto3")
