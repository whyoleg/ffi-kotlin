plugins {
    id("foreignbuild.conventions.multiplatform.library")
    id("foreignbuild.conventions.multiplatform.targets.tools")
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
