package dev.whyoleg.foreign.tool.clang

import dev.whyoleg.foreign.tool.clang.api.*
import kotlinx.serialization.*

public expect object ClangCompiler {
    public fun buildIndex(
        headers: Set<String>,
        compilerArgs: List<String>,
        outputPath: String? = null
    ): CxIndex
}

@Serializable
internal sealed class ClangCommand {
    @Serializable
    data class BuildIndex(
        val headers: Set<String>,
        val compilerArgs: List<String>,
    ) : ClangCommand()
}
