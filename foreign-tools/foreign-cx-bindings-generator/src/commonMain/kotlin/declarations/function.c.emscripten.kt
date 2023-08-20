package dev.whyoleg.foreign.cx.bindings.generator.declarations

import dev.whyoleg.foreign.cx.index.*

internal fun CxFunctionInfo.toCEmscriptenDeclaration(
    index: CxIndex
): String = buildString {
    append("EMSCRIPTEN_KEEPALIVE ")
        .append(
            when {
                returnType.type.isRecord(index) -> "void"
                else                            -> returnType.name
            }
        ).append(" ").append(prefixedName).append("(")

    parametersWithReturnType(index).joinToIfNotEmpty(
        this,
        prefix = "\n",
        separator = ",\n",
        postfix = "\n"
    ) { parameter ->
        "$INDENT${parameter.type.name} p_${parameter.name}"
    }
    append(") {\n")
    append(INDENT)
        .append(cReturnBlock(index, null, null))
        .append(name.value).append("(")
    parameters.joinToIfNotEmpty(
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
