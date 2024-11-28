plugins {
    id("foreignbuild.kotlin-gradle")
    `java-gradle-plugin`
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.android.gradle.plugin)

    implementation(projects.foreignGradleWorker)
}

gradlePlugin {
    plugins {
        create("dev.whyoleg.foreign") {
            id = "dev.whyoleg.foreign"
            implementationClass = "dev.whyoleg.foreign.gradle.ForeignPlugin"
        }
    }
}
