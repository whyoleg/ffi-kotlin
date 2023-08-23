plugins {
    id("foreignbuild.multiplatform-library")
    id("foreignbuild.desktop-tool")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.foreignCxMetadata)
            }
        }
    }
}
