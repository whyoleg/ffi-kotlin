import foreignbuild.*

plugins {
    id("foreignbuild.kotlin-gradle")
    `java-gradle-plugin`
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.android.gradle.plugin)
    compileOnly(projects.foreignGradleWorker)
}

gradlePlugin {
    plugins {
        create("dev.whyoleg.foreign") {
            id = "dev.whyoleg.foreign"
            implementationClass = "dev.whyoleg.foreign.gradle.ForeignPlugin"
        }
    }
}

registerGenerateConstantsTask("generateForeignConstants", kotlin.sourceSets.main) {
    packageName = "dev.whyoleg.foreign.gradle"
    className = "ForeignConstants"
    properties.put(
        "version",
        provider { project.version.toString() }
    )
}
