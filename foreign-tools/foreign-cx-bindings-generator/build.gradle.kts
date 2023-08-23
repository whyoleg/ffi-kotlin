plugins {
    id("foreignbuild.conventions.multiplatform.library")
    id("foreignbuild.tools.targets")
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
