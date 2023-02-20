import de.undercouch.gradle.tasks.download.*

plugins {
    id("build-parameters")

    alias(kotlinLibs.plugins.multiplatform) apply false
}

buildscript {
    dependencies {
        classpath("de.undercouch:gradle-download-task:5.3.0")
    }
}

val skipTest = buildParameters.skip.test
val skipLink = buildParameters.skip.link
subprojects {
    if (skipTest) tasks.matching { it.name.endsWith("test", ignoreCase = true) }.configureEach { onlyIf { false } }
    if (skipLink) tasks.matching { it.name.startsWith("link", ignoreCase = true) }.configureEach { onlyIf { false } }
}

val downloadOpenssl3 by tasks.registering(Download::class) {
    src("https://github.com/whyoleg/openssl-builds/releases/download/3.0.8-build-1/openssl3-all.zip")
    onlyIfModified(true)
    overwrite(false)
    dest(layout.buildDirectory.file("openssl3/prebuilt.zip"))
}

val prepareOpenssl3 by tasks.registering(Sync::class) {
    from(downloadOpenssl3.map { zipTree(it.outputFiles.single()) })
    into(layout.buildDirectory.dir("openssl3/prebuilt"))
}
