package dev.whyoleg.foreign.tool.codegen

import dev.whyoleg.foreign.tool.cbridge.api.*

//    @JvmStatic
//    external fun EVP_MD_fetch(ctx: Long, algorithm: Long, properties: Long): Long
internal fun KotlinCodeBuilder.jvmJniExternalFunction(
    function: CFunction
) {
    annotation("JvmStatic")
    raw("private external fun ${function.description.name}")
    if (function.parameters.isNotEmpty()) raw("()")
    else {
        raw("(\n")
        indented {
            function.parameters.forEach { parameter ->
                raw("${parameter.name}: ${parameter.type.toKotlinJniType()},\n")
            }
        }
        raw(")")
    }
    if (!function.returnType.isVoid()) raw(": ${function.returnType.toKotlinJniType()}")
    raw("\n")
}

private fun CType.toKotlinJniType(): String {
    return when (this) {
        CType.Void           -> TODO("WTF?")
        is CType.Array       -> "Long" // pointer
        is CType.Pointer     -> "Long"

        is CType.Enum        -> "Long"
        is CType.Number      -> TODO()
        is CType.Record      -> TODO()
        is CType.Typedef     -> TODO()

        is CType.Mixed       -> TODO()
        is CType.Unsupported -> TODO()
    }
}
