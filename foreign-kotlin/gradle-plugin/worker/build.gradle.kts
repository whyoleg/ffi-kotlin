plugins {
    id("foreignbuild.kotlin-gradle")
}

dependencies {
    api(projects.foreignToolingClang)
    api(projects.foreignToolingCodegen)
}
