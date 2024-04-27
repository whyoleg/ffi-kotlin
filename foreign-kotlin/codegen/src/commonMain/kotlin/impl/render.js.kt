package dev.whyoleg.foreign.codegen.impl

import dev.whyoleg.foreign.bridge.c.*
import dev.whyoleg.foreign.codegen.*
import dev.whyoleg.foreign.codegen.kotlin.*

internal fun KotlinCodeBuilder.jsFunctionBody(
    function: CDeclaration<CFunction>
): KotlinCodeBuilder = apply {
    if (!function.data.returnType.isVoid) {
        raw("return ")
    }
    raw("${function.headerName.replace('/', '_')}.${function.name}")
    // TODO: handle struct/pointer like
    if (function.data.parameters.isEmpty()) {
        raw("()")
    } else {
        raw("(\n")
        indented {
            function.data.parameters.forEach { parameter ->
//                raw("${renderParameter(parameter.name, parameter.type)},\n")
            }
        }
        raw(")")
    }
}
