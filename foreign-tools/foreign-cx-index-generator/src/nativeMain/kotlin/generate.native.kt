package dev.whyoleg.foreign.index.cx.generator

import dev.whyoleg.foreign.index.cx.*
import dev.whyoleg.foreign.index.cx.generator.internal.*
import okio.*
import okio.ByteString.Companion.encodeUtf8

public actual fun CxIndex.Companion.generate(
    headers: Set<String>,
    includePaths: Set<String>
): CxIndex {
    val headersFile = createTemporaryHeadersFile(headers)
    val compilerArgs = includePaths.map { "-I$it" }

    val builder = CxIndexBuilder()

    useIndex { index ->
        useTranslationUnit(index, headersFile, compilerArgs) { translationUnit ->
            CxIndexRunner(builder).run(index, translationUnit)
        }
    }

    return builder.build()
}

private fun createTemporaryHeadersFile(headers: Set<String>): String {
    val fileInfo = headers.joinToString("\n") { "#include <$it>" }
    val hashInfo = fileInfo.encodeUtf8().sha1().hex()
    val headersPath = FileSystem.SYSTEM_TEMPORARY_DIRECTORY.resolve("$hashInfo.h")
    val headersExists = FileSystem.SYSTEM.exists(headersPath)
    if (!headersExists) FileSystem.SYSTEM.write(headersPath, mustCreate = true) { writeUtf8(fileInfo) }
    return headersPath.toString()
}
