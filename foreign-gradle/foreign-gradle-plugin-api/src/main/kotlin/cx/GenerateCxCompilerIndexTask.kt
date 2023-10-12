package dev.whyoleg.foreign.gradle.api.cx

import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.provider.*
import org.gradle.api.tasks.*

public interface GenerateCxCompilerIndexTask : Task {
    @get:Input
    public val target: Provider<CxTarget>

    @get:Input
    public val dependencies: MapProperty<CxDependency, FileSystemLocation>

    @get:OutputDirectory
    public val destinationDirectory: DirectoryProperty
}
