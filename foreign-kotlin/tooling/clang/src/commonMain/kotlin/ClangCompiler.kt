package dev.whyoleg.foreign.tooling.clang

import dev.whyoleg.foreign.tooling.cxapi.*
import kotlinx.io.*
import kotlinx.io.files.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.json.io.*

public expect object ClangCompiler {
    public fun buildIndex(
        headers: Set<String>,
        compilerArgs: List<String>
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

private val json = Json {
    prettyPrint = true
}

@OptIn(ExperimentalSerializationApi::class)
internal inline fun <reified T> decode(path: Path): T {
    return SystemFileSystem.source(path).buffered().use {
        json.decodeFromSource(it)
    }
}

@OptIn(ExperimentalSerializationApi::class)
internal inline fun <reified T> encode(path: Path, value: T) {
    SystemFileSystem.createDirectories(path.parent!!)
    return SystemFileSystem.sink(path).buffered().use {
        json.encodeToSink(value, it)
    }
}
