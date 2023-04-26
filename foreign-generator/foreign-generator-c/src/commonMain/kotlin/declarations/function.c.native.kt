package dev.whyoleg.foreign.generator.c.declarations

import dev.whyoleg.foreign.cx.index.*

//TODO: struct support
internal fun CxFunctionInfo.toCNativeBitcodeDeclaration(): String = buildString {
    append("const void* ").append(prefixedName).append("_call ")
    append("__asm(\"").append(prefixedName).append("\");\n")
    append("const void* ").append(prefixedName).append("_call = (const void*)&").append(name.value).append(";")
}
