/**
 * run to check for dependencies:
 *  ./gradlew dependencyUpdates --init-script gradle/libs.updates.gradle.kts
 */

initscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.github.ben-manes:gradle-versions-plugin:+")
    }
}

rootProject {
    println("Project: $name")
    apply<com.github.benmanes.gradle.versions.VersionsPlugin>()

    tasks.named("dependencyUpdates") {
        gradle.includedBuilds.forEach {
            dependsOn(it.task(":dependencyUpdates"))
        }
    }
}
