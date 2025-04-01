package dev.whyoleg.foreign.tool.serialization

import kotlinx.io.*
import kotlinx.io.files.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.json.io.*

private val json = Json {
    prettyPrint = true
    classDiscriminator = "@kind"
}

@OptIn(ExperimentalSerializationApi::class)
public inline fun <reified T> Path.decode(): T = decode(serializer())

@OptIn(ExperimentalSerializationApi::class)
public fun <T> Path.decode(serializer: KSerializer<T>): T {
    return SystemFileSystem.source(this).buffered().use {
        json.decodeFromSource(serializer, it)
    }
}

@OptIn(ExperimentalSerializationApi::class)
public inline fun <reified T> Path.encode(value: T): Unit = encode(serializer(), value)

@OptIn(ExperimentalSerializationApi::class)
public fun <T> Path.encode(serializer: KSerializer<T>, value: T) {
    SystemFileSystem.createDirectories(parent!!)
    return SystemFileSystem.sink(this).buffered().use {
        json.encodeToSink(serializer, value, it)
    }
}
