package dev.whyoleg.foreign.gradle.worker

import dev.whyoleg.foreign.tooling.clang.*
import org.gradle.api.file.*
import org.gradle.api.provider.*
import org.gradle.workers.*

public abstract class ClangBuildIndexAction : WorkAction<ClangBuildIndexAction.Parameters> {
    public interface Parameters : WorkParameters {
        public val headers: SetProperty<String>
        public val compilerArgs: ListProperty<String>
        public val outputFile: RegularFileProperty
    }

    override fun execute() {
        ClangCompiler.buildIndex(
            headers = parameters.headers.get(),
            compilerArgs = parameters.compilerArgs.get(),
            outputPath = parameters.outputFile.get().asFile.absolutePath
        )
    }
}
