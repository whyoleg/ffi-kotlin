import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    id("buildx-multiplatform")
}

kotlin {
    //TODO: setup 20 toolchain only for FFM compilation - is it possible?
    jvmToolchain(20) //for FFM

    jvm("jvmJdkFfm") {
        attributes.attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 20)
        compilations.all {
            compilerOptions.configure {
                jvmTarget.set(JvmTarget.JVM_19) //20 is not yet supported
            }
        }
    }
}
