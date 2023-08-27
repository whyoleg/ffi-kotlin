package dev.whyoleg.foreign.cx.index.generator

import kotlinx.cinterop.*
import kotlinx.serialization.json.*
import platform.posix.*

// the method should be public, or it will not be available for c export
@Deprecated("Only for JNI", level = DeprecationLevel.HIDDEN)
@CName("generateCxIndex")
public fun generateCxIndex(
    argumentsBytes: CPointer<ByteVar>?,
    argumentsBytesSize: Int,
    resultBytesSize: CPointer<IntVar>?,
): CPointer<ByteVar>? {
    val result = try {
        val arguments = Json.decodeFromString(
            deserializer = GenerateCxIndexArguments.serializer(),
            string = argumentsBytes?.readBytes(argumentsBytesSize)?.decodeToString() ?: ""
        )
        val index = generateCxIndex(
            headerFilePath = arguments.headerFilePath,
            compilerArgs = arguments.compilerArgs
        )
        Json.encodeToString(
            serializer = GenerateCxIndexResult.serializer(),
            value = GenerateCxIndexResult.Success(index)
        ).encodeToByteArray()
    } catch (cause: Throwable) {
        try {
            Json.encodeToString(
                serializer = GenerateCxIndexResult.serializer(),
                value = GenerateCxIndexResult.Failure(cause.message, cause.stackTraceToString())
            ).encodeToByteArray()
        } catch (otherCause: Throwable) {
            try {
                otherCause.printStackTrace()
            } catch (ignore: Throwable) {
            }
            return null
        }
    }

    if (result.isEmpty()) return null
    val destination = malloc(result.size.convert()) ?: return null

    result.usePinned { memcpy(destination, it.addressOf(0), result.size.convert()) }
    resultBytesSize?.pointed?.value = result.size

    return destination.reinterpret<ByteVar>()
}
