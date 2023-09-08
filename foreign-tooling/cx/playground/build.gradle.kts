plugins {
    id("foreignbuild.multiplatform-library")
    id("foreignbuild.desktop-tool")
    alias(kotlinLibs.plugins.plugin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.cx.foreignToolingCxCompiler)

                implementation(libs.okio)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.serialization.json.okio)
            }
        }
    }
}
