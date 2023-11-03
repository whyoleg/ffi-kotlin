plugins {
    alias(kotlinLibs.plugins.jvm)
}

kotlin {
    explicitApi()
    jvmToolchain(8)
}

dependencies {
    compileOnly(projects.foreignGradleTooling)

    implementation("dev.whyoleg.foreign:foreign-tooling-cx-compiler-runner")
    implementation(libs.kotlinx.serialization.json)
}
