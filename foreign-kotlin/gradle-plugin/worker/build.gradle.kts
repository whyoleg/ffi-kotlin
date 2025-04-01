plugins {
    id("foreignbuild.kotlin-gradle")
}

dependencies {
    api(projects.foreignToolClang)
    api(projects.foreignToolCbridge)
    api(projects.foreignToolCodegen)
}
