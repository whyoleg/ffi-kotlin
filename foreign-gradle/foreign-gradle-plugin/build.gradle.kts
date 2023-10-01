import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    alias(kotlinLibs.plugins.jvm)
    `java-gradle-plugin`
}

kotlin {
    explicitApi()
    jvmToolchain(8)
    compilerOptions {
        // for compatibility with Gradle 8+
        apiVersion.set(KotlinVersion.KOTLIN_1_8)
        languageVersion.set(KotlinVersion.KOTLIN_1_8)
    }
}

dependencies {
    api(projects.foreignGradlePluginApi)

    compileOnly(gradleKotlinDsl())

    compileOnly(kotlinLibs.gradle.plugin)
    compileOnly(libs.build.android)

    compileOnly("dev.whyoleg.foreign:foreign-tooling-cx-compiler-runner")
}

gradlePlugin {
    plugins {
        create("dev.whyoleg.foreign") {
            id = "dev.whyoleg.foreign"
            implementationClass = "dev.whyoleg.foreign.gradle.ForeignPlugin"
        }
    }
}
