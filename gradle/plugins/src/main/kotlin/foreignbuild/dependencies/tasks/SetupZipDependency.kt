package foreignbuild.dependencies.tasks

import org.gradle.api.file.*
import org.gradle.api.tasks.*
import javax.inject.*

abstract class SetupZipDependency @Inject constructor(
    private val archiveOperations: ArchiveOperations
) : Sync() {
    private val configuration = project.configurations.detachedConfiguration()

    init {
        @Suppress("LeakingThis")
        from(project.provider {
            archiveOperations.zipTree(configuration.singleFile)
        })
    }

    fun dependency(value: String) {
        configuration.dependencies.add(project.dependencies.create(value))
    }
}
