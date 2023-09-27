plugins {
    id("foreignbuild.multiplatform-library")
    id("foreignbuild.desktop-tool")
    alias(kotlinLibs.plugins.plugin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.cx.foreignToolingCxBridgeModel)
            }
        }
        commonTest {
            dependencies {
                implementation(projects.cx.foreignToolingCxCompilerTestSupport)
                implementation(libs.kotlinx.serialization.json)
            }
        }
    }
}
