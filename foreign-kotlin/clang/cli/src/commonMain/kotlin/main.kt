package dev.whyoleg.foreign.clang.cli

import dev.whyoleg.foreign.clang.api.*
import dev.whyoleg.foreign.clang.cli.commands.*
import dev.whyoleg.foreign.clang.compiler.*
import kotlinx.io.*
import kotlinx.io.files.*
import kotlin.random.*

fun main(args: Array<String>) {
    when (val command = ClangCommand.decode(args.single())) {
        is ClangCommand.BuildIndex -> {
            val headersPath = createHeadersFile(command.headers)
            val index = ClangCompiler.buildIndex(headersPath, command.compilerArgs)
            saveIndex(command.outputPath, index)
        }
    }
}

private fun saveIndex(outputPath: String, index: CxIndex) {
    val indexString = CxIndex.encode(index)
    SystemFileSystem.sink(Path(outputPath)).buffered().use { output ->
        output.writeString(indexString)
    }
}

private fun createHeadersFile(headers: Set<String>): String {
    val headersContent = buildString {
        headers.forEach { header ->
            appendLine("#include <$header>")
        }
    }
    val headersPath = Path(SystemTemporaryDirectory, "headers-${Random.nextInt()}.h")
    SystemFileSystem.sink(headersPath).buffered().use { output ->
        output.writeString(headersContent)
    }
    return headersPath.toString()
}
