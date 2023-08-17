plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("build:parameters")
    implementation(kotlinLibs.gradle.plugin)
    implementation(libs.build.android)
    implementation(libs.build.tukaani.xz)
}
