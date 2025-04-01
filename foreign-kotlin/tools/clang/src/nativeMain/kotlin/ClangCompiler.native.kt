package dev.whyoleg.foreign.tool.clang

import dev.whyoleg.foreign.tool.clang.api.*
import dev.whyoleg.foreign.tool.serialization.*
import kotlinx.io.files.*

public actual object ClangCompiler {
    public actual fun buildIndex(
        headers: Set<String>,
        compilerArgs: List<String>,
        outputPath: String?
    ): CxIndex {
        val index = useIndex { index ->
            val indexer = ClangIndexer()
            useTranslationUnit(index, headers, compilerArgs, indexer::indexTranslationUnit)
            indexer.buildIndex()
        }
        outputPath?.let(::Path)?.encode(index)
        return index
    }
}

@Deprecated("for JVM usage", level = DeprecationLevel.HIDDEN)
public fun main(args: Array<String>) {
    require(args.size == 2)

    val inputFilePath = Path(args[0])
    val outputFilePath = Path(args[1])

    when (val command = inputFilePath.decode<ClangCommand>()) {
        is ClangCommand.BuildIndex -> outputFilePath.encode(
            ClangCompiler.buildIndex(command.headers, command.compilerArgs)
        )
    }
}
