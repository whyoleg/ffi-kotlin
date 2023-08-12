package foreignbuild.conventions.multiplatform.targets

plugins {
    kotlin("multiplatform")
}

kotlin {
    js {
        nodejs()
        browser()
    }
}
