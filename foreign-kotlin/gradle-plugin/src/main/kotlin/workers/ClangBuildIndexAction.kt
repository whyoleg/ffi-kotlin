package dev.whyoleg.foreign.gradle.workers

import dev.whyoleg.foreign.tooling.clang.*
import org.gradle.api.file.*
import org.gradle.api.provider.*
import org.gradle.workers.*

internal abstract class ClangBuildIndexAction : WorkAction<ClangBuildIndexAction.Parameters> {
    interface Parameters : WorkParameters {
        val headers: SetProperty<String>
        val compilerArgs: ListProperty<String>
        val outputFile: RegularFileProperty
    }

    override fun execute() {
        ClangCompiler.buildIndex(
            headers = parameters.headers.get(),
            compilerArgs = parameters.compilerArgs.get(),
            outputPath = parameters.outputFile.get().asFile.absolutePath
        )
    }
}
