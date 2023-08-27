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

@Serializable // TODO: decide on what should be here
internal data class GenerateCxIndexResult(
    val index: CxIndex?,
    val error: String?
)

// TODO: drop okio dependency, or move it to test
internal expect val SystemFileSystem: FileSystem
