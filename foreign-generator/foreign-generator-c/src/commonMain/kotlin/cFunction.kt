package dev.whyoleg.foreign.generator.c

import dev.whyoleg.foreign.cx.index.*

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

internal fun CxFunctionInfo.returnBlock(
    index: CxIndex,
    castReturnPointer: String?,
    castReturnValue: String?
): String = when {
    returnType.type.isRecord(index) -> when (castReturnPointer) {
        null -> "*p_return_pointer = "
        else -> "*($castReturnPointer *)p_return_pointer = "
    }
    else                            -> when (castReturnValue) {
        null -> "return "
        else -> "return ($castReturnValue) "
    }
}
