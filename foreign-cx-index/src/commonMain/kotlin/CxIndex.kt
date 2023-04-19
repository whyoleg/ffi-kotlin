package dev.whyoleg.foreign.cx.index

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.json.okio.*
import okio.*

@Serializable
public data class CxIndex(
    val builtIn: CxHeaderInfo,
    val headers: List<CxHeaderInfo> = emptyList()
)

private val json = Json {
    useAlternativeNames = false
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
    val json = Json(json) {
        prettyPrint = true
        prettyPrintIndent = "  "
    }

    fun writeHeader(path: Path, headerInfo: CxHeaderInfo) {
        createDirectories(path.parent!!)
        write(path) {
            json.encodeToBufferedSink(CxHeaderInfo.serializer(), headerInfo, this)
        }
    }

    deleteRecursively(path)
    writeHeader(path.resolve("_.json"), index.builtIn)
    index.headers.forEach { header ->
        writeHeader(path.resolve(header.name.value.replace(".h", ".json")), header)
    }
}

@OptIn(ExperimentalSerializationApi::class)
public fun FileSystem.readCxIndex(path: Path): CxIndex = read(path) {
    json.decodeFromBufferedSource(CxIndex.serializer(), this)
}
