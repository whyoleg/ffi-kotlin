plugins {
    id("foreignbuild.multiplatform-library")
    id("foreignbuild.desktop-tool")
    alias(kotlinLibs.plugins.plugin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.foreignCxIndex)
                implementation(projects.foreignCxIndexGenerator)
                implementation(projects.foreignCxBindings)
                // need to fix code there
                // implementation(projects.foreignCxBindingsGenerator)

                implementation(libs.okio)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.serialization.json.okio)
            }
        }
    }
}
