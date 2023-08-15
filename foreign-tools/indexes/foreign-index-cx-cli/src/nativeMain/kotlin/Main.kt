package dev.whyoleg.foreign.index.cx.cli

import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.parameters.options.*
import dev.whyoleg.foreign.index.cx.*
import dev.whyoleg.foreign.index.cx.cli.internal.*
import okio.*
import okio.ByteString.Companion.encodeUtf8
import okio.Path.Companion.toPath

fun main(args: Array<String>): Unit = Main().main(args)

class Main : CliktCommand() {
    private val header by option().multiple(required = true).unique()
    private val include by option().multiple().unique()
    private val verbose by option().flag()
    private val output by option().required()

    override fun run() {
        val headersFile = createTemporaryHeadersFile(header)
        val compilerArgs = include.map { "-I$it" }

        val builder = CxIndexBuilder()

        useIndex { index ->
            useTranslationUnit(index, headersFile, compilerArgs) { translationUnit ->
                CxIndexRunner(builder).run(index, translationUnit)
            }
        }
        val index = builder.build()

        FileSystem.SYSTEM.writeCxIndex(output.toPath(), index)
        if (verbose) {
            FileSystem.SYSTEM.writeCxIndexVerbose("$output-verbose".toPath(), index)
        }
    }
}

private fun createTemporaryHeadersFile(headers: Set<String>): String {
    val fileInfo = headers.joinToString("\n") { "#include <$it>" }
    val hashInfo = fileInfo.encodeUtf8().sha1().hex()
    val headersPath = FileSystem.SYSTEM_TEMPORARY_DIRECTORY.resolve("$hashInfo.h")
    val headersExists = FileSystem.SYSTEM.exists(headersPath)
    if (!headersExists) FileSystem.SYSTEM.write(headersPath, mustCreate = true) { writeUtf8(fileInfo) }
    return headersPath.toString()
}
