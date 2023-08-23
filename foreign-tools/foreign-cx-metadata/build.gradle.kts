plugins {
    id("foreignbuild.conventions.multiplatform.library")
    id("foreignbuild.tools.targets")
    alias(kotlinLibs.plugins.plugin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.foreignCxIndex)
            }
        }
    }
}