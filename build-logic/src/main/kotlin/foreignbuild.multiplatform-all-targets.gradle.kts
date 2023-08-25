import org.jetbrains.kotlin.gradle.targets.js.dsl.*

plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {

    }
    js {
        nodejs()
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasm {
        nodejs()
        browser()
    }

    // native desktop
    macosArm64()
    macosX64()
    linuxX64()

    // native ios
    iosArm64()
    iosSimulatorArm64()
    iosX64()
}
