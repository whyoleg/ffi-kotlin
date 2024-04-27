plugins {
    id("foreignbuild.multiplatform-tool")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.foreignBridgeC)
        }
        commonTest.dependencies {
            implementation(projects.foreignClangArguments)
            implementation(libs.kotlinx.io.core)
            implementation(kotlin("test"))
        }
    }
}
