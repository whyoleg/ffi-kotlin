plugins {
    id("buildx-multiplatform-base")
    alias(kotlinLibs.plugins.plugin.serialization)
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
                api(projects.foreignCxIndex)
            }
        }
    }
}
