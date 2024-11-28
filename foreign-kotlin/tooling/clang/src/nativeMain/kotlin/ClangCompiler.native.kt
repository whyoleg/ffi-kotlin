package dev.whyoleg.foreign.tooling.clang

import dev.whyoleg.foreign.tooling.cxapi.*
import kotlinx.io.*
import kotlinx.io.files.*
import kotlin.uuid.*

public actual object ClangCompiler {
    public actual fun buildIndex(
        headers: Set<String>,
        compilerArgs: List<String>,
        outputPath: String?
    ): CxIndex {
        val index = useIndex { index ->
            val indexer = ClangIndexer()
            useTranslationUnit(index, createHeadersFile(headers), compilerArgs, indexer::indexTranslationUnit)
            indexer.buildIndex()
        }
        outputPath?.let { encode(Path(it), index) }
        return index
    }
}

@OptIn(ExperimentalUuidApi::class)
private fun createHeadersFile(headers: Set<String>): String {
    val headersContent = buildString {
        headers.forEach { header ->
            appendLine("#include <$header>")
        }
    }
    val headersPath = Path(SystemTemporaryDirectory, "headers-${Uuid.random()}.h")
    SystemFileSystem.sink(headersPath).buffered().use { output ->
        output.writeString(headersContent)
    }
    return headersPath.toString()
}

@Deprecated("for JVM usage", level = DeprecationLevel.HIDDEN)
public fun main(args: Array<String>) {
    require(args.size == 2)

    val inputFilePath = Path(args[0])
    val outputFilePath = Path(args[1])

    when (val command = decode<ClangCommand>(inputFilePath)) {
        is ClangCommand.BuildIndex -> encode(
            outputFilePath,
            ClangCompiler.buildIndex(command.headers, command.compilerArgs)
        )
    }
}
