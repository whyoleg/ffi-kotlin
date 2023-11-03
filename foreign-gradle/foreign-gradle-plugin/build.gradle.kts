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
    compileOnly(gradleKotlinDsl())

    compileOnly(kotlinLibs.gradle.plugin)
    compileOnly(libs.build.android)

    api(projects.foreignGradlePluginApi)
    compileOnly(projects.foreignGradleInternalTool) // needed for workers
}

gradlePlugin {
    plugins {
        create("dev.whyoleg.foreign") {
            id = "dev.whyoleg.foreign"
            implementationClass = "dev.whyoleg.foreign.gradle.api.ForeignPlugin"
        }
        create("dev.whyoleg.foreign.internal") {
            id = "dev.whyoleg.foreign.internal"
            implementationClass = "dev.whyoleg.foreign.gradle.InternalForeignPlugin"
        }
    }
}
