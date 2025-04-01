plugins {
    id("foreignbuild.kotlin-tool")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.foreignToolCbridgeApi)

            implementation(projects.foreignToolSerialization)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
