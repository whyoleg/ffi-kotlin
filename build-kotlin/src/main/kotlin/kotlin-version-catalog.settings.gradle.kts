plugins {
    id("build-parameters")
}

val kotlinVersion = "1.9.0-Beta"
val kotlinVersionOverride = the<buildparameters.BuildParametersExtension>().kotlin.override.version.orNull

if (kotlinVersionOverride != null) logger.lifecycle("Kotlin version override: $kotlinVersionOverride")

pluginManagement {
    if (kotlinVersionOverride != null) repositories {
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
    }
}

dependencyResolutionManagement {
    if (kotlinVersionOverride != null) repositories {
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
    }
    versionCatalogs {
        create("kotlinLibs") {
            val kotlin = version("kotlin", kotlinVersionOverride ?: kotlinVersion)

            plugin("multiplatform", "org.jetbrains.kotlin.multiplatform").versionRef(kotlin)
            plugin("jvm", "org.jetbrains.kotlin.jvm").versionRef(kotlin)
            plugin("plugin.serialization", "org.jetbrains.kotlin.plugin.serialization").versionRef(kotlin)

            fun lib(name: String, artifact: String) = library(name, "org.jetbrains.kotlin", artifact).versionRef(kotlin)

            lib("gradle-plugin", "kotlin-gradle-plugin")
            lib("compiler", "kotlin-compiler")
            lib("compiler-test-framework", "kotlin-compiler-internal-test-framework")
        }
    }
}
