plugins {
    alias(kotlinLibs.plugins.jvm)
}

kotlin {
    explicitApi()
    jvmToolchain(8)
}

dependencies {
    compileOnly(gradleApi())
    // TODO: compileOnly vs api vs implementation
    compileOnly(projects.foreignGradlePluginApi)

    implementation("dev.whyoleg.foreign:foreign-tooling-cx-compiler-runner")
    implementation(libs.kotlinx.serialization.json)
}
