package dev.whyoleg.foreign.codegen.kotlin

import dev.whyoleg.foreign.bridge.c.*
import dev.whyoleg.foreign.codegen.*

internal fun KotlinCodeBuilder.jsFunctionBody(
    function: CFunction
): KotlinCodeBuilder = apply {
    if (!function.returnType.isVoid()) {
        raw("return ")
    }
    raw("${function.description.headerName.replace('/', '_')}.${function.description.ktName}")
    // TODO: handle struct/pointer like

    if (function.parameters.isEmpty()) {
        raw("()")
    } else {
        raw("(\n")
        indented {
            function.parameters.forEach { parameter ->
//                raw("${renderParameter(parameter.name, parameter.type)},\n")
            }
        }
        raw(")")
    }
}

internal fun KotlinCodeBuilder.jvmFfmFunctionsHolder(
    index: CFragmentIndex,
    headerName: String,
    functions: List<CFunction>
) {
    raw("internal external object $headerName {\n")
    indented {
        functions.forEach { function ->
            annotation("JsName", listOf("\"_ffi_${function.description.ktName}\""))
            raw("fun ${function.description.ktName}")
            if (function.parameters.isEmpty()) {
                raw("()")
            } else {
                raw("(\n")
                indented {
                    function.parameters.forEach {
                        raw("${it.ktName}: ${it.type.asKotlinFunctionInteropTypeString(index)},\n")
                    }
                }
                raw(")")
            }
            if (!function.returnType.isVoid()) {
                raw(": ${function.returnType.asKotlinFunctionInteropTypeString(index)}")
            }
            raw("\n")
        }
    }
    raw("}\n")


    //@JsModule("foreign-crypto-wasm")
    //@JsNonModule
    //@JsName("Module")
    //private external object err {
    //    @JsName("_ffi_ERR_get_error")
    //    fun ERR_get_error(): Int
    //
    //    @JsName("_ffi_ERR_error_string")
    //    fun ERR_error_string(e: Int, buf: Int): Int
    //}

    //
//    @WasmImport("foreign-crypto-wasm", "ffi_ERR_get_error")
//    private external fun ffi_ERR_get_error(): PlatformInt
//
//    @WasmImport("foreign-crypto-wasm", "ffi_ERR_error_string")
//    private external fun ffi_ERR_error_string(e: PlatformInt, buf: MemoryAddress): MemoryAddress
}

private fun CType.asKotlinFunctionInteropTypeString(index: CFragmentIndex): String = when (this) {
    CType.Void           -> "Unit"
    CType.Boolean        -> TODO() // TODO?
    is CType.Pointer     -> "Int"
    is CType.Array       -> "Int"
    is CType.Enum        -> "Int"
    is CType.Number      -> "Int" // TODO
    is CType.Record      -> "Int" // pointer
    is CType.Typedef     -> index.typedefs.getValue(id).resolvedType.asKotlinFunctionInteropTypeString(index)

    is CType.Mixed       -> TODO()
    is CType.Function    -> TODO()
    is CType.Unsupported -> TODO()
}