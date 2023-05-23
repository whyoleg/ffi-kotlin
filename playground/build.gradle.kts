import dev.whyoleg.foreign.gradle.*
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.*

plugins {
    kotlin("multiplatform")
}

buildscript {
    dependencies {
        classpath("dev.whyoleg.foreign:foreign-gradle-plugin:0.1.0")
    }
}

apply<ForeignCompilerGradlePlugin>()

kotlin {
    jvm()
    js {
        nodejs()
    }
    macosArm64()

    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }

    sourceSets {
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

//tasks.withType<CompileUsingKotlinDaemon>().configureEach {
//    compilerExecutionStrategy.set(KotlinCompilerExecutionStrategy.IN_PROCESS)
//}
