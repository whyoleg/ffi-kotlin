plugins {
    `kotlin-dsl`
}

kotlin {
    jvmToolchain(8)
    explicitApi()
}

dependencies {
    compileOnly(kotlinLibs.gradle.plugin)
    compileOnly(libs.build.android)

    implementation("dev.whyoleg.foreign:foreign-generator-cx")
}
