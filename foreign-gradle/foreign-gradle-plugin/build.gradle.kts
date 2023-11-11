import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    alias(kotlinLibs.plugins.jvm)
    `java-gradle-plugin`
    alias(libs.plugins.buildconfig)
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
    compileOnly(kotlinLibs.gradle.plugin)
    compileOnly(libs.build.android)

    api(projects.foreignGradleTooling)
    // used for workers, implementation is provided in place
    compileOnly(projects.foreignGradleInternalTool)
}

gradlePlugin {
    plugins {
        create("dev.whyoleg.foreign") {
            id = "dev.whyoleg.foreign"
            implementationClass = "dev.whyoleg.foreign.gradle.ForeignPlugin"
        }
    }
}

buildConfig {
    packageName("dev.whyoleg.foreign.gradle.internal")
    useKotlinOutput {
        topLevelConstants = true
        internalVisibility = true
    }
    buildConfigField("String", "FOREIGN_VERSION", "\"$version\"")
}

tasks.maybeCreate("prepareKotlinIdeaImport").dependsOn(tasks.generateBuildConfig)
