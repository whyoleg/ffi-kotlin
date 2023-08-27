package dev.whyoleg.foreign.cx.index.generator

import kotlinx.serialization.json.*
import okio.*
import okio.Path.Companion.toPath
import kotlin.native.runtime.*

// TODO: decide on better way to return result - not via file...
// TODO: for some reason no other way to passing result back (via pointers) is working and crashes JVM...
// TODO: sometimes it still crashes - I have no idea what's going on...
// TODO: TBD what's going on here...
// the method should be public, or it will not be available for c export
@Deprecated("Only for JNI", level = DeprecationLevel.HIDDEN)
@CName("generateCxIndex")
public fun generateCxIndex(
    argumentsString: String,
    resultPathString: String
) {
    try {
        val arguments = Json.decodeFromString(GenerateCxIndexArguments.serializer(), argumentsString)
        val index = generateCxIndex(
            headerFilePath = arguments.headerFilePath,
            compilerArgs = arguments.compilerArgs
        )
        FileSystem.SYSTEM.write(resultPathString.toPath()) {
            writeUtf8(Json.encodeToString(GenerateCxIndexResult.serializer(), GenerateCxIndexResult(index, null)))
        }
    } catch (cause: Throwable) {
        try {
            FileSystem.SYSTEM.write(resultPathString.toPath()) {
                writeUtf8(Json.encodeToString(GenerateCxIndexResult.serializer(), GenerateCxIndexResult(null, cause.stackTraceToString())))
            }
        } catch (otherCause: Throwable) {
            try {
                otherCause.printStackTrace()
            } catch (ignore: Throwable) {
            }
        }
    }

    // we should call GC here to not interfere with JVM and JNI
    // - or we will have JVM crash
    // - why?
    // - not really sure :(
    @OptIn(NativeRuntimeApi::class)
    GC.collect()
}
