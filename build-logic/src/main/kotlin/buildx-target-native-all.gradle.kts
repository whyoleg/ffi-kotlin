plugins {
    id("buildx-target-native-desktop")
}

kotlin {
    @Suppress("DEPRECATION")
    iosArm32()
    iosArm64()
    iosSimulatorArm64()
    iosX64()
}
