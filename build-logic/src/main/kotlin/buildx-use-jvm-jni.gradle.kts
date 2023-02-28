plugins {
    id("buildx-target-jvm-jni")
}

val buildJni by tasks.registering(jni.DefaultBuildJni::class)

kotlin {
    sourceSets {
        val jvmJniMain by getting {
            resources.srcDir(buildJni.map { it.outputDirectory })
        }
    }
}
