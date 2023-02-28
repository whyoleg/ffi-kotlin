import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    id("buildx-multiplatform")
}

kotlin {
    jvm("jvmPanama") {
        attributes.attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 20)
        compilations.all {
            compilerOptions.configure {
                jvmTarget.set(JvmTarget.JVM_19) //20 is not yet supported
            }
        }
    }
}
