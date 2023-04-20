package dev.whyoleg.foreign.generator.c

import dev.whyoleg.foreign.cx.index.*

//TODO: struct support
internal fun CxFunctionInfo.toCEmscriptenDeclaration(): String = buildString {
    append("EMSCRIPTEN_KEEPALIVE ").append(returnType.name).append(" ").append(prefixedName).append("(")
    if (parameters.isNotEmpty()) parameters.joinTo(
        this,
        prefix = "\n",
        separator = ",\n",
        postfix = "\n"
    ) { parameter ->
        "$INDENT${parameter.type.name} p_${parameter.name}"
    }
    append(") {\n")
    append(INDENT).append("return ").append(name.value).append("(")
    if (parameters.isNotEmpty()) parameters.joinTo(
        this,
        prefix = "\n",
        separator = ",\n",
        postfix = "\n$INDENT"
    ) { parameter ->
        "$INDENT${INDENT}p_${parameter.name}"
    }
    append(");")
    append("\n}")
}
