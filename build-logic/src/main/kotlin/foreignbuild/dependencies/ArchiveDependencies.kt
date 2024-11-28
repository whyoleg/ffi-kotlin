package foreignbuild.dependencies

import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.*
import org.gradle.kotlin.dsl.support.*
import java.io.*

fun Project.registerZipDependencySetupTask(
    name: String,
    dependency: String,
    action: Action<Sync>
): TaskProvider<Sync> = registerArchiveDependencySetupTask(name, dependency, action) {
    zipTree(it)
}

fun Project.registerTarGzDependencySetupTask(
    name: String,
    dependency: String,
    action: Action<Sync>
): TaskProvider<Sync> = registerArchiveDependencySetupTask(name, dependency, action) {
    tarTree(gzip(it))
}

private fun Project.registerArchiveDependencySetupTask(
    name: String,
    dependency: String,
    action: Action<Sync>,
    provide: ArchiveOperations.(File) -> Any
): TaskProvider<Sync> {
    val configuration = configurations.create("${name}Dependency")
    dependencies.add(configuration.name, dependency)
    val archiveOperations = project.serviceOf<ArchiveOperations>()
    return tasks.register<Sync>(name) {
        from(provider { archiveOperations.provide(configuration.singleFile) })
        into(temporaryDir)
        includeEmptyDirs = false
        action.execute(this)
    }
}
