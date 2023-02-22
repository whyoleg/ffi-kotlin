package openssl

import org.gradle.api.*
import org.gradle.api.provider.*
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.*
import java.io.*

open class OpensslExtension(
    @Transient val rootProject: Project,
) {

    val prepareOpensslTaskProvider = rootProject.tasks.named<Sync>(OpensslRootPlugin.PREPARE_OPENSSL_TASK_NAM)

    fun includeDir(target: String): Provider<File> {
        return prepareOpensslTaskProvider.map { it.destinationDir.resolve(target).resolve("include") }
    }

    fun libDir(target: String): Provider<File> {
        return prepareOpensslTaskProvider.map { it.destinationDir.resolve(target).resolve("lib") }
    }
}
