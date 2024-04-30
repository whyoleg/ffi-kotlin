package dev.whyoleg.foreign.codegen.kotlin

import dev.whyoleg.foreign.bridge.c.*
import dev.whyoleg.foreign.codegen.*

internal fun KotlinCodeBuilder.jsFunctionBody(
    function: CFunction
): KotlinCodeBuilder = apply {
    if (!function.returnType.isVoid) {
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
