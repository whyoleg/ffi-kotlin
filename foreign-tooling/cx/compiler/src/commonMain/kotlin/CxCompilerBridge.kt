package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

internal object CxCompilerBridge {
    @Serializable
    data class Response<R : Any>(
        val value: R?,
        val errorStackTrace: String?
    )

    @Serializable
    sealed class Request {
        sealed class Typed<T> : Request()

        @Serializable
        data class BuildIndex(
            val mainFileName: String,
            val mainFilePath: String,
            val compilerArgs: List<String>
        ) : Typed<CxCompilerIndex>()
    }

    fun <T> decode(
        deserializer: DeserializationStrategy<T>,
        bytes: ByteArray
    ): T {
        return Json.decodeFromString(deserializer, bytes.decodeToString())
    }

    fun <T> encode(
        serializer: SerializationStrategy<T>,
        value: T
    ): ByteArray {
        return Json.encodeToString(serializer, value).encodeToByteArray()
    }
}
