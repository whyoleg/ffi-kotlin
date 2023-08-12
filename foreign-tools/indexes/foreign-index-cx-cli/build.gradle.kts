plugins {
    id("foreignbuild.conventions.multiplatform.base")
    //TODO: enable other native targets
    //id("foreignbuild.conventions.multiplatform.targets.native.desktop")
    alias(kotlinLibs.plugins.multiplatform)
}

kotlin {
    macosArm64 {
        compilations.named("main") {
            cinterops.create("declarations") {
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
