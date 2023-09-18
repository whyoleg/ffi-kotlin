import foreignbuild.settings.*

val kotlinVersionOverride = providers.gradleProperty("foreignbuild.kotlin-version").orNull?.takeIf(String::isNotBlank)

if (kotlinVersionOverride != null) logger.lifecycle("Kotlin version override: $kotlinVersionOverride")

pluginManagement {
    if (kotlinVersionOverride != null) repositories {
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev") {
            content {
                includeGroupAndSubgroups("org.jetbrains.kotlin")
            }
        }
    }
}

dependencyResolutionManagement {
    if (kotlinVersionOverride != null) repositories {
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev") {
            content {
                includeGroupAndSubgroups("org.jetbrains.kotlin")
            }
        }
    }
    versionCatalogs {
        create("kotlinLibs") {
            val kotlin = version("kotlin", kotlinVersionOverride ?: kotlinVersion)

            library("gradle-plugin", "org.jetbrains.kotlin", "kotlin-gradle-plugin").versionRef(kotlin)

            plugin("multiplatform", "org.jetbrains.kotlin.multiplatform").versionRef(kotlin)
            plugin("jvm", "org.jetbrains.kotlin.jvm").versionRef(kotlin)
            plugin("plugin.serialization", "org.jetbrains.kotlin.plugin.serialization").versionRef(kotlin)
        }
    }
}
