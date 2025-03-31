plugins {
    id("foreignbuild.kotlin-tool")
    alias(libs.plugins.kotlin.plugin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.core)
        }
    }
}
