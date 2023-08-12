pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "build-settings"

includeBuild("../parameters") {
    dependencySubstitution {
        substitute(module("build:parameters")).using(project(":"))
    }
}
