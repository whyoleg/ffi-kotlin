plugins {
    id("foreignbuild.kotlin-tool")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.serialization.core)
            api(libs.kotlinx.io.core)

            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.serialization.json.io)
        }
    }
}
