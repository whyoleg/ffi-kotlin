plugins {
    id("foreignbuild.multiplatform-library")
    id("foreignbuild.desktop-tool")
    alias(kotlinLibs.plugins.plugin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.cx.foreignToolingCxCompilerModel)
                implementation(projects.cx.foreignToolingCxCompiler)

                api(libs.okio)
            }
        }
    }
}
