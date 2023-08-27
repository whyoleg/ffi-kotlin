package dev.whyoleg.foreign.cx.index.generator

import dev.whyoleg.foreign.cx.index.*
import dev.whyoleg.foreign.cx.index.generator.internal.*
import okio.*
import okio.ByteString.Companion.encodeUtf8

internal actual val SystemFileSystem: FileSystem get() = FileSystem.SYSTEM

internal actual fun generateCxIndex(
    headerFilePath: String,
    compilerArgs: List<String>
): CxIndex {
    val builder = CxIndexBuilder()

    useIndex { index ->
        useTranslationUnit(index, headerFilePath, compilerArgs) { translationUnit ->
            CxIndexRunner(builder).run(index, translationUnit)
        }
    }

    return builder.build()
}
