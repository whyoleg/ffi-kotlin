plugins {
    id("kotlin-version-catalog")
    id("org.gradle.toolchains.foojay-resolver-convention")
}

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

gradle.root().rootProject { configure(rootDir) }

fun Gradle.root(): Gradle = parent?.root() ?: this

fun configure(rootDir: File) {
    dependencyResolutionManagement {
        versionCatalogs {
            create("libs") {
                from(files(rootDir.resolve("gradle/libs.versions.toml")))
            }
        }
    }
}
