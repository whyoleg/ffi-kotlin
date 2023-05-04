plugins {
    id("buildx-multiplatform-base")
    alias(kotlinLibs.plugins.plugin.serialization)
}

kotlin {
    explicitApi()

    jvm()
    js {
        nodejs() //just to have all platforms here
    }
    macosArm64()
    macosX64()

    sourceSets {
        commonMain {
            dependencies {
                api("com.squareup.okio:okio:3.3.0")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json-okio:1.5.0")
            }
        }
    }
}
