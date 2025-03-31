plugins {
    id("foreignbuild.kotlin-tool")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.foreignToolClangApi)
            api(projects.foreignToolCbridgeApi)

            implementation(libs.kotlinx.io.core)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
