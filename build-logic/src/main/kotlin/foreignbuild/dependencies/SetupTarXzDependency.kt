package foreignbuild.dependencies

import org.gradle.api.file.*
import org.gradle.api.internal.file.archive.compression.*
import org.gradle.api.tasks.*
import org.tukaani.xz.*
import java.io.*
import java.net.*
import javax.inject.*

abstract class SetupTarXzDependency @Inject constructor(
    private val archiveOperations: ArchiveOperations
) : Sync() {
    private val configuration = project.configurations.detachedConfiguration()

    init {
        @Suppress("LeakingThis")
        from(project.provider {
            archiveOperations.tarTree(XzArchiver(configuration.singleFile))
        })
    }

    fun dependency(value: String) {
        configuration.dependencies.add(project.dependencies.create(value))
    }
}

private class XzArchiver(private val file: File) : CompressedReadableResource {
    override fun read(): InputStream = XZInputStream(file.inputStream().buffered())
    override fun getURI(): URI = URIBuilder(file.toURI()).schemePrefix("xz:").build()
    override fun getBackingFile(): File = file
    override fun getBaseName(): String = file.name
    override fun getDisplayName(): String = file.path
}
