plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(kotlinLibs.gradle.plugin)
    implementation(libs.build.android)
    implementation("org.tukaani:xz:1.9")
}
