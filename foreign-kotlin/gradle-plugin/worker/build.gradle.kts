plugins {
    id("foreignbuild.kotlin-gradle")
}

dependencies {
    api(projects.foreignToolClangCompiler)
    api(projects.foreignToolCbridgeCodegen)
}
