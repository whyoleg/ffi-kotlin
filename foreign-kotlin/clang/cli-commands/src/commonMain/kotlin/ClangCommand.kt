package dev.whyoleg.foreign.clang.cli.commands

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
public sealed class ClangCommand {
    @Serializable
    public data class BuildIndex(
        val headers: Set<String>,
        val compilerArgs: List<String>,
        val outputPath: String,
    ) : ClangCommand()

    public companion object {
        @OptIn(ExperimentalSerializationApi::class)
        private val json = Json {
            prettyPrint = true
            prettyPrintIndent = "  "
        }

        public fun encode(command: ClangCommand): String = json.encodeToString(serializer(), command)
        public fun decode(string: String): ClangCommand = json.decodeFromString(serializer(), string)
    }
}
