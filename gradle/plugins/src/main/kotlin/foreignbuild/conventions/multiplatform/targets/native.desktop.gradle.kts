package foreignbuild.conventions.multiplatform.targets

plugins {
    kotlin("multiplatform")
}

kotlin {
    macosArm64()
    macosX64()
    linuxX64()
}
