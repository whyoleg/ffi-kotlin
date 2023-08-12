plugins {
    id("buildx-multiplatform-base")
    alias(kotlinLibs.plugins.multiplatform)
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
            entryPoint = "dev.whyoleg.foreign.index.cx.cli.main"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.indexes.foreignIndexCx)
                implementation(libs.clikt)
            }
        }
    }
}
