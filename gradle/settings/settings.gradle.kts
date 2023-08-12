pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "build-settings"

includeBuild("../parameters") {
    dependencySubstitution {
        substitute(module("build:parameters")).using(project(":"))
    }
}
