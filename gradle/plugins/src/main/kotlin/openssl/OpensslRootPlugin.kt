package openssl

import org.gradle.api.*

class OpensslRootPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = target.run {
        check(this == rootProject)

//        val downloadOpenssl by tasks.registering(Download::class) {
//            src("https://github.com/whyoleg/openssl-builds/releases/download/3.0.8-build-2/openssl3-all.zip")
//            onlyIfModified(true)
//            dest(layout.buildDirectory.file("openssl/prebuilt.zip"))
//        }
//
//        tasks.register(PREPARE_OPENSSL_TASK_NAM, Sync::class) {
//            from(downloadOpenssl.map { zipTree(it.outputFiles.single()) })
//            into(layout.buildDirectory.dir("openssl/prebuilt"))
//        }
    }

    companion object {
        val PREPARE_OPENSSL_TASK_NAM = "prepareOpenssl"
    }
}
