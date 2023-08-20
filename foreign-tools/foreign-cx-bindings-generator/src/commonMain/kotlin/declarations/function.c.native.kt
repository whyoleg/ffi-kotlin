package dev.whyoleg.foreign.cx.bindings.generator.declarations

import dev.whyoleg.foreign.cx.index.*

private val CxDeclarationInfo.interopName get() = "interop_$prefixedName"

internal fun CxFunctionInfo.toCNativeDeclaration(
    index: CxIndex
): String = buildString {
    append("__attribute__((always_inline)) ").append(
        when {
            returnType.type.isRecord(index) -> "void"
            else                            -> returnType.name
        }
    ).append(" ").append(interopName).append("(")

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
    append(");").appendLine()
    append("}").appendLine().appendLine()

    append("const void* ").append(prefixedName).append("_call ")
    append("__asm(\"").append(prefixedName).append("\");\n")
    append("const void* ").append(prefixedName).append("_call = (const void*)&").append(interopName).append(";")
}
