package foreignbuild.conventions.multiplatform.targets

import org.jetbrains.kotlin.gradle.targets.js.dsl.*

plugins {
    kotlin("multiplatform")
}

@OptIn(ExperimentalWasmDsl::class)
kotlin {
    wasm {
        nodejs()
        browser()
    }
}
