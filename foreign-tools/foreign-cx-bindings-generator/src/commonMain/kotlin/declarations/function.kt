package dev.whyoleg.foreign.generator.cx.declarations

import dev.whyoleg.foreign.index.cx.*

internal fun <T> List<T>.joinToIfNotEmpty(
    builder: StringBuilder,
    separator: CharSequence,
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    transform: (T) -> CharSequence
): StringBuilder {
    if (isNotEmpty()) joinTo(builder, separator, prefix, postfix, transform = transform)
    return builder
}

internal fun CxFunctionInfo.parametersWithReturnType(index: CxIndex): List<CxFunctionInfo.Parameter> {
    if (!returnType.type.isRecord(index)) return parameters

    return parameters + CxFunctionInfo.Parameter(
        name = "return_pointer",
        type = CxTypeInfo(
            returnType.name?.let { "$it *" },
            CxType.Pointer(returnType.type)
        )
    )
}
