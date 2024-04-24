import foreignbuild.*

plugins {
    id("foreignbuild.multiplatform-library")
}

kotlin {
    // JVM for Gradle plugin
    jvmTarget(17)
    // Native for CLI
    nativeDesktopTargets()
}
