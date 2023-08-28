plugins {
    id("foreignbuild.multiplatform-library")
    id("foreignbuild.desktop-tool")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.foreignCxBindings)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                // for temp FS support
                implementation(projects.foreignCxStorage)
            }
        }
    }
}
