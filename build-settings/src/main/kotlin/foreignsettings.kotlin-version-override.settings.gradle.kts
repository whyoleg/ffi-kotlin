@file:Suppress("UnstableApiUsage")

val kotlinVersionOverride =
    providers.gradleProperty("foreignbuild.kotlinVersionOverride").orNull?.takeIf(String::isNotBlank)

if (kotlinVersionOverride != null) {
    val kotlinDevRepository = "https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev"
    val kotlinGroup = "org.jetbrains.kotlin"

    logger.lifecycle("Kotlin version override: $kotlinVersionOverride, repository: $kotlinDevRepository")

    pluginManagement {
        repositories {
            maven(kotlinDevRepository) {
                content { includeGroupAndSubgroups(kotlinGroup) }
            }
        }
    }

    dependencyResolutionManagement {
        repositories {
            maven(kotlinDevRepository) {
                content { includeGroupAndSubgroups(kotlinGroup) }
            }
        }

        versionCatalogs {
            named("libs") {
                version("kotlin", kotlinVersionOverride)
            }
        }
    }
}
