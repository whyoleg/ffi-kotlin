plugins {
    id("foreignbuild.conventions.multiplatform.library")
    id("foreignbuild.conventions.multiplatform.targets.all-no-wasm")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.schemas.foreignSchemaCx)
            }
        }
    }
}
