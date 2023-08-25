package foreignbuild.dependencies

import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.internal.file.archive.compression.*
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.*
import org.tukaani.xz.*
import java.io.*
import java.net.*
import javax.inject.*

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

fun Project.registerTarXzDependencySetupTask(
    name: String,
    dependency: String,
    action: Action<Sync>
): TaskProvider<Sync> = registerArchiveDependencySetupTask(name, dependency, {
    action.execute(this)
    notCompatibleWithConfigurationCache("something with XZ ???")
}) {
    tarTree(XzArchiver(it))
}

private fun Project.registerArchiveDependencySetupTask(
    name: String,
    dependency: String,
    action: Action<Sync>,
    provide: ArchiveOperations.(File) -> Any
): TaskProvider<Sync> {
    val configuration = configurations.create("${name}Dependency")
    dependencies.add(configuration.name, dependency)
    val archiveOperations = objects.newInstance<Injected>().archiveOperations
    return tasks.register<Sync>(name) {
        from(provider { archiveOperations.provide(configuration.singleFile) })
        into(temporaryDir)
        includeEmptyDirs = false
        action.execute(this)
    }
}

private abstract class Injected @Inject constructor(
    val archiveOperations: ArchiveOperations
)

private class XzArchiver(private val file: File) : CompressedReadableResource {
    override fun read(): InputStream = XZInputStream(file.inputStream().buffered())
    override fun getURI(): URI = URIBuilder(file.toURI()).schemePrefix("xz:").build()
    override fun getBackingFile(): File = file
    override fun getBaseName(): String = file.name
    override fun getDisplayName(): String = file.path
}
