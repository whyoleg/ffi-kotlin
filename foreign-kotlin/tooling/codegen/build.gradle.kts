plugins {
    id("foreignbuild.kotlin-tool")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.foreignToolingCxapi)
            api(projects.foreignToolingCbridge)

            implementation(libs.kotlinx.io.core)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
