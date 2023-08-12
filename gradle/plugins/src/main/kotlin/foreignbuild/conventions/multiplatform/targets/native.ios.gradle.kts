package foreignbuild.conventions.multiplatform.targets

plugins {
    kotlin("multiplatform")
}

kotlin {
    iosArm64()
    iosSimulatorArm64()
    iosX64()
}
