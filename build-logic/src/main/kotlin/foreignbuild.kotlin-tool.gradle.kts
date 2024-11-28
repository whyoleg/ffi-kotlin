plugins {
    id("foreignbuild.kotlin")
    kotlin("multiplatform")
}

kotlin {
    // JVM for Gradle plugin
    jvmToolchain(17)
    jvm()
    // Native desktop targets for CLI
    linuxX64()
    mingwX64()
    macosX64()
    macosArm64()
}
