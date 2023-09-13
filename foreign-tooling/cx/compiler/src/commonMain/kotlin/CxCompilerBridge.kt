package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

// TODO: may be replace bridge with protobuf/cbor? - check performance and payload sizes
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
            val mainFilePath: String,
            val compilerArgs: List<String>
        ) : Typed<CxIndex>()
    }

    private val json = Json {
        classDiscriminator = "_type"
    }

    fun <T> decode(
        deserializer: DeserializationStrategy<T>,
        bytes: ByteArray
    ): T {
        return json.decodeFromString(deserializer, bytes.decodeToString())
    }

    fun <T> encode(
        serializer: SerializationStrategy<T>,
        value: T
    ): ByteArray {
        return json.encodeToString(serializer, value).encodeToByteArray()
    }
}
