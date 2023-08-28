package dev.whyoleg.foreign.cx.index.generator

import dev.whyoleg.foreign.cx.index.*
import kotlinx.serialization.*

internal object CxIndexGenerator {
    @Serializable
    internal data class Arguments(
        val headerFilePath: String,
        val compilerArgs: List<String>
    )

    @Serializable
    internal sealed class Result {
        @Serializable
        data class Success(val index: CxIndex) : Result()

        @Serializable
        data class Failure(val message: String?, val stackTrace: String) : Result()
    }
}

// entry point
internal expect fun CxIndexGenerator.generate(arguments: CxIndexGenerator.Arguments): CxIndexGenerator.Result
