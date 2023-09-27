plugins {
    id("foreignbuild.multiplatform-library")
    id("foreignbuild.desktop-tool")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.cx.foreignToolingCxCompiler)
                api(projects.cx.foreignToolingCxCompilerModel)
                api(projects.cx.foreignToolingCxCompilerRunner)
            }
        }
    }
}
