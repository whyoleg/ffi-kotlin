plugins {
    id("foreignbuild.conventions.multiplatform.library")
    id("foreignbuild.conventions.multiplatform.targets.tools")
    alias(kotlinLibs.plugins.plugin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(libs.okio)
                api(libs.kotlinx.serialization.json)
                api(libs.kotlinx.serialization.json.okio)
            }
        }
    }
}
