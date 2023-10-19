pluginManagement {
    includeBuild("../build-settings")
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

rootProject.name = "foreign-gradle"

includeBuild("../foreign-tooling")

// plugin - plugin artifact
//    api      - compileOnly - TODO?
//    internal - runtimeOnly
// plugin-api - public api
// plugin-internal - internal impl
//    internal-tool - compileOnly + classpathIsolation
// plugin-internal-tool - internal tool for workers

include("foreign-gradle-plugin")
include("foreign-gradle-plugin-api")
include("foreign-gradle-internal-tool")
