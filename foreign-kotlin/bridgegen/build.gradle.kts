plugins {
    id("foreignbuild.multiplatform-tool")
    alias(libs.plugins.kotlin.plugin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.foreignBridgeC)
            api(projects.foreignClangApi)
        }
    }
}
