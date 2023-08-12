plugins {
    id("build-parameters")
}

val kotlinVersion = "1.9.0"
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
            val kotlinVersion = version("kotlin", kotlinVersionOverride ?: kotlinVersion)
            fun kotlinPlugin(name: String) = plugin(name, "org.jetbrains.kotlin.$name").versionRef(kotlinVersion)
            fun kotlinLibrary(name: String, artifact: String) = library(name, "org.jetbrains.kotlin", artifact).versionRef(kotlinVersion)

            kotlinPlugin("multiplatform")
            kotlinPlugin("jvm")
            kotlinPlugin("plugin.serialization")

            kotlinLibrary("gradle-plugin", "kotlin-gradle-plugin")
            kotlinLibrary("compiler", "kotlin-compiler")
            kotlinLibrary("compiler-test-framework", "kotlin-compiler-internal-test-framework")
        }
    }
}
