plugins {
    id("foreignbuild.multiplatform-library")
    id("foreignbuild.desktop-tool")
    alias(kotlinLibs.plugins.plugin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.cx.foreignToolingCxModel)
                implementation(projects.cx.foreignToolingCxCompiler)

                implementation(libs.okio)
            }
        }
    }
}
