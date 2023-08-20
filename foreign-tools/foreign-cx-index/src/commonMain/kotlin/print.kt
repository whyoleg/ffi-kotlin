package dev.whyoleg.foreign.cx.index

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.json.okio.*
import okio.*

private val json = Json {
    useAlternativeNames = false
}

@OptIn(ExperimentalSerializationApi::class)
private val prettyJson = Json(json) {
    prettyPrint = true
    prettyPrintIndent = "  "
}

public fun <T> T.toPrettyString(serializer: KSerializer<T>): String = prettyJson.encodeToString(serializer, this)

public inline fun <reified T> T.toPrettyString(): String = toPrettyString(serializer())

@OptIn(ExperimentalSerializationApi::class)
public fun FileSystem.readCxIndex(path: Path): CxIndex = read(path) {
    json.decodeFromBufferedSource(CxIndex.serializer(), this)
}

@OptIn(ExperimentalSerializationApi::class)
public fun FileSystem.writeCxIndex(path: Path, index: CxIndex) {
    createDirectories(path.parent!!)
    write(path) {
        json.encodeToBufferedSink(CxIndex.serializer(), index, this)
    }
}

@OptIn(ExperimentalSerializationApi::class)
public fun FileSystem.writeCxIndexVerbose(path: Path, index: CxIndex) {
    fun writeHeader(path: Path, headerInfo: CxHeaderInfo) {
        createDirectories(path.parent!!)
        write(path) {
            prettyJson.encodeToBufferedSink(CxHeaderInfo.serializer(), headerInfo, this)
        }
    }

    deleteRecursively(path)
    index.headers.forEach { header ->
        writeHeader(path.resolve(header.name.value.replace(".h", ".json")), header)
    }
}
