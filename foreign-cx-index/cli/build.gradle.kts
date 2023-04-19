plugins {
    id("buildx-multiplatform-base")
}

kotlin {
    //TODO: add other native targets
    macosArm64 {
        val main by compilations.getting {
            val declarations by cinterops.creating {
                defFile("src/commonMain/interop/clang.def")
            }
        }

        binaries.executable {
            entryPoint = "dev.whyoleg.foreign.cx.index.cli.main"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.foreignCxIndex)
                implementation("com.github.ajalt.clikt:clikt:3.5.2")
            }
        }
    }
}
