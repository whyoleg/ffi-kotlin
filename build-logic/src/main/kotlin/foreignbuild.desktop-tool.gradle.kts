plugins {
    kotlin("multiplatform")
}

kotlin {
    jvmToolchain(8)

    jvm()

    // native desktop targets
    macosArm64()
    macosX64()
    linuxX64()
    mingwX64()
}
