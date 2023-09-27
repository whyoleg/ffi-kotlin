package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import kotlinx.serialization.*

public actual object CxCompiler {
    public actual fun buildIndex(
        mainFileName: String,
        mainFilePath: String,
        compilerArgs: List<String>
    ): CxCompilerIndex = call(CxCompilerBridge.Request.BuildIndex(mainFileName, mainFilePath, compilerArgs))

    private inline fun <reified T> call(request: CxCompilerBridge.Request.Typed<T>): T = call(request, serializer())

    private fun <T> call(request: CxCompilerBridge.Request.Typed<T>, responseSerializer: KSerializer<T>): T {
        val response = CxCompilerBridge.decode(
            deserializer = CxCompilerBridge.Response.serializer(responseSerializer),
            bytes = CxCompilerJni.call(
                CxCompilerBridge.encode(
                    serializer = CxCompilerBridge.Request.serializer(),
                    value = request
                )
            ) ?: error("Native execution failure: NULL")
        )
        response.value?.let { return it }
        error("Native execution failure: ${response.errorStackTrace}")
    }
}
