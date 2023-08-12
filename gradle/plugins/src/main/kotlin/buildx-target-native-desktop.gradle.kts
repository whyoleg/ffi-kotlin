plugins {
    id("buildx-multiplatform")
}

kotlin {
    macosArm64()
    macosX64()
    linuxX64()
    mingwX64()
}
