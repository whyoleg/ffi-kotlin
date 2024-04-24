import foreignsettings.*

pluginManagement {
    includeBuild("../build-logic")
    includeBuild("../build-settings")
    includeBuild("../foreign-kotlin")
}

plugins {
    id("foreignsettings.default")
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

projects("samples") {
    folder("libcrypto") {
        module("api")
    }
}
