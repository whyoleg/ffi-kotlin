import foreignbuild.*

plugins {
    id("foreignbuild.kotlin-gradle")
}

dependencies {
    compileOnly(gradleApi())
    compileOnly(kotlin("stdlib"))
    compileOnly(projects.foreignGradleWorkerClasspath)
}

registerGenerateConstantsTask("generateWorkerConstants", kotlin.sourceSets.main) {
    packageName = "dev.whyoleg.foreign.gradle.worker"
    className = "WorkerConstants"
    properties.put(
        "VERSION",
        provider { project.version.toString() }
    )
}
