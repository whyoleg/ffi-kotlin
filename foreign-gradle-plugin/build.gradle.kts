plugins {
    `kotlin-dsl`
}

kotlin {
    jvmToolchain(8)
    explicitApi()
}

dependencies {
    compileOnly(kotlinLibs.gradle.plugin)
    compileOnly("com.android.tools.build:gradle:7.3.0")
}
