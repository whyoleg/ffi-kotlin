package foreignbuild.tools

import org.jetbrains.kotlin.gradle.*

plugins {
    kotlin("multiplatform")
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()
    jvm {

    }

    // native desktop targets
    macosArm64()
    macosX64()
    linuxX64()
}
