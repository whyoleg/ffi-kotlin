pluginManagement {
    includeBuild("build-settings")
}

plugins {
    id("foreignbuild.settings.default")
}

rootProject.name = "ffi-kotlin"

//includeBuild("foreign-runtime")
includeBuild("foreign-tooling")
includeBuild("foreign-gradle")

//includeBuild("test-projects")


// TODO: decide on root project structure
//  - do we really need to have separate included builds?
// build-logic
// build-settings
// foreign-projects
// test-projects
// examples/samples
// docs
