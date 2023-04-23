package dev.whyoleg.foreign.schema.c

import dev.whyoleg.foreign.cx.index.*
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

@OptIn(ExperimentalSerializationApi::class)
public fun FileSystem.readForeignCLibrary(path: Path): ForeignCLibrary = read(path) {
    json.decodeFromBufferedSource(ForeignCLibrary.serializer(), this)
}

@OptIn(ExperimentalSerializationApi::class)
public fun FileSystem.writeForeignCLibrary(path: Path, library: ForeignCLibrary) {
    createDirectories(path.parent!!)
    write(path) {
        json.encodeToBufferedSink(ForeignCLibrary.serializer(), library, this)
    }
}

@OptIn(ExperimentalSerializationApi::class)
public fun FileSystem.writeForeignCLibraryVerbose(path: Path, library: ForeignCLibrary) {
    fun writePackage(path: Path, pkg: ForeignCPackage) {
        createDirectories(path.parent!!)
        write(path) {
            prettyJson.encodeToBufferedSink(ForeignCPackage.serializer(), pkg, this)
        }
    }

    deleteRecursively(path)
    writeCxIndexVerbose(path.resolve("index"), library.index)
    library.packages.forEach { pkg ->
        writePackage(path.resolve("packages").resolve(pkg.name.replace(".", "/") + ".json"), pkg)
    }
}
