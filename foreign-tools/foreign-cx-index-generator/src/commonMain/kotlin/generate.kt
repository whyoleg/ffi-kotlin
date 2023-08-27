package dev.whyoleg.foreign.cx.index.generator

import dev.whyoleg.foreign.cx.index.*
import kotlinx.serialization.*
import okio.*

internal expect fun generateCxIndex(
    headerFilePath: String,
    compilerArgs: List<String>
): CxIndex

@Serializable
internal data class GenerateCxIndexArguments(
    val headerFilePath: String,
    val compilerArgs: List<String>
)

@Serializable
internal sealed class GenerateCxIndexResult {
    @Serializable
    data class Success(val index: CxIndex) : GenerateCxIndexResult()

    @Serializable
    data class Failure(val message: String?, val stackTrace: String) : GenerateCxIndexResult()
}

// TODO: drop okio dependency, or move it to test
internal expect val SystemFileSystem: FileSystem
