plugins {
    id("foreignbuild.multiplatform-tool")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.foreignBridgeC)
        }
    }
}
