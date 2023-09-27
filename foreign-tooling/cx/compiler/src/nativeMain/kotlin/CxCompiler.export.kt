package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import kotlinx.cinterop.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import platform.posix.*
import kotlin.experimental.*

// TODO: sometimes bridge can fail JVM - hard to reproduce - may be need to switch to std allocator
// the method should be public, or it will not be available for c export
@OptIn(ExperimentalSerializationApi::class, ExperimentalNativeApi::class)
@Deprecated("Only for JNI", level = DeprecationLevel.HIDDEN)
@CName("callCxCompiler")
public fun callCxCompiler(
    requestBytes: CPointer<ByteVar>?,
    requestBytesSize: Int,
    response: CPointerVar<ByteVar>?,
): Int { // returns' size of the response
    if (response == null) return -1;

    val responseBytes = try {
        checkNotNull(requestBytes) { "Request pointer should be provided" }

        when (val request = CxCompilerBridge.decode(
            deserializer = CxCompilerBridge.Request.serializer(),
            bytes = requestBytes.readBytes(requestBytesSize)
        )) {
            is CxCompilerBridge.Request.BuildIndex -> {
                CxCompilerBridge.encode(
                    serializer = CxCompilerBridge.Response.serializer(CxCompilerIndex.serializer()),
                    value = CxCompilerBridge.Response(
                        value = CxCompiler.buildIndex(
                            mainFileName = request.mainFileName,
                            mainFilePath = request.mainFilePath,
                            compilerArgs = request.compilerArgs
                        ),
                        errorStackTrace = null
                    )
                )
            }
        }
    } catch (cause: Throwable) {
        try {
            CxCompilerBridge.encode(
                serializer = CxCompilerBridge.Response.serializer(NothingSerializer()),
                value = CxCompilerBridge.Response(
                    value = null,
                    errorStackTrace = cause.stackTraceToString()
                )
            )
        } catch (otherCause: Throwable) {
            try {
                cause.addSuppressed(otherCause)
                cause.printStackTrace()
            } catch (ignore: Throwable) {

            }
            null
        }
    }

    if (responseBytes == null || responseBytes.isEmpty()) return -1
    val destination = malloc(responseBytes.size.convert()) ?: return -1
    responseBytes.usePinned { memcpy(destination, it.addressOf(0), responseBytes.size.convert()) }
    response.value = destination.reinterpret()
    return responseBytes.size
}
