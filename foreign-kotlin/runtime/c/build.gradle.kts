import foreignbuild.*

plugins {
    id("foreignbuild.multiplatform-library")
}

kotlin {
    jvmTarget(8)
    nativeTargets()
    webTargets()

    sourceSets {
        commonMain.dependencies {
            api(projects.foreignRuntimeCore)
        }
    }
}
