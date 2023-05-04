plugins {
    id("buildx-multiplatform-base")
}

kotlin {
    explicitApi()

    jvm()
    js {
        nodejs() //just to have all platforms here
    }
    macosArm64()
    macosX64()

    sourceSets {
        commonMain {
            dependencies {
                api(projects.foreignTools.indexes.foreignIndexCx)
                api(projects.foreignTools.schemas.foreignSchemaCx)
            }
        }
    }
}
