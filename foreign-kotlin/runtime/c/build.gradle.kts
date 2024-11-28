import foreignbuild.*

plugins {
    id("foreignbuild.kotlin")
    kotlin("multiplatform")
}

kotlin {
    jvmToolchain(8)

    jvmTarget()
    nativeTargets()
    webTargets()

    sourceSets {
        commonMain.dependencies {
            api(projects.foreignRuntimeCore)
        }
    }
}
