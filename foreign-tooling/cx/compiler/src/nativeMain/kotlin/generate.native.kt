package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.internal.*
import kotlinx.cinterop.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import platform.posix.*

internal actual fun CxIndexGenerator.generate(arguments: CxIndexGenerator.Arguments): CxIndexGenerator.Result = try {
    val builder = CxIndexBuilder()

    useIndex { index ->
        useTranslationUnit(index, arguments.headerFilePath, arguments.compilerArgs) { translationUnit ->
            CxIndexRunner(builder).run(index, translationUnit)
        }
    }

    val index = builder.build()
    CxIndexGenerator.Result.Success(index)
} catch (cause: Throwable) {
    CxIndexGenerator.Result.Failure(cause.message, cause.stackTraceToString())
}

// the method should be public, or it will not be available for c export
@Deprecated("Only for JNI", level = DeprecationLevel.HIDDEN)
@CName("generateCxIndexBridge")
public fun generateCxIndexBridge(
    argumentsBytes: CPointer<ByteVar>?,
    argumentsBytesSize: Int,
    resultBytesSize: CPointer<IntVar>?,
): CPointer<ByteVar>? {
    // result calculation should not throw!
    val result = try {
        val argumentsString = argumentsBytes?.readBytes(argumentsBytesSize)?.decodeToString() ?: error("NULL arguments passed")
        val arguments = Json.decodeFromString<CxIndexGenerator.Arguments>(argumentsString)
        val result = CxIndexGenerator.generate(arguments)
        Json.encodeToString<CxIndexGenerator.Result>(result).encodeToByteArray()
    } catch (cause: Throwable) {
        try {
            Json.encodeToString<CxIndexGenerator.Result>(
                CxIndexGenerator.Result.Failure("Unexpected bridge failure: ${cause.message}", cause.stackTraceToString())
            ).encodeToByteArray()
        } catch (otherCause: Throwable) {
            try {
                otherCause.printStackTrace()
            } catch (ignore: Throwable) {
            }
            null
        }
    }
    return returnByteArray(result, resultBytesSize)
}

// should not throw!
private fun returnByteArray(result: ByteArray?, resultBytesSize: CPointer<IntVar>?): CPointer<ByteVar>? {
    if (result == null || result.isEmpty()) return null
    val destination = malloc(result.size.convert())?.reinterpret<ByteVar>() ?: return null

    result.usePinned { memcpy(destination, it.addressOf(0), result.size.convert()) }
    resultBytesSize?.pointed?.value = result.size

    return destination
}
