plugins {
    id("buildx-target-native-desktop")
}

kotlin {
    // needed only to be able to set up working with Int with different bit width
    @Suppress("DEPRECATION")
    iosArm32()
    iosArm64()
    iosSimulatorArm64()
    iosX64()
}
