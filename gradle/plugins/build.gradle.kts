plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("build:parameters")
    implementation(kotlinLibs.gradle.plugin)
    implementation(libs.build.gradle.download)
    implementation(libs.build.android)
}
