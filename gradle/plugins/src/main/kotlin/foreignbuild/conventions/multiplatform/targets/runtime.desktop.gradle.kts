package foreignbuild.conventions.multiplatform.targets

plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {

    }
    // native desktop
    macosArm64()
    macosX64()
    linuxX64()
}
