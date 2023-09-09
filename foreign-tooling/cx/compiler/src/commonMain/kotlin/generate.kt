package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.model.*
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

public fun generateCxIndex(
    headerFilePath: String,
    compilerArgs: List<String>
): CxIndex = when (val result = CxIndexGenerator.generate(CxIndexGenerator.Arguments(headerFilePath, compilerArgs))) {
    is CxIndexGenerator.Result.Success -> result.index
    is CxIndexGenerator.Result.Failure -> error("CxIndex generation failure: ${result.message}\n${result.stackTrace}")
}
