package dev.whyoleg.foreign.generator.c.declarations

import dev.whyoleg.foreign.cx.index.*

internal fun CxFunctionInfo.cReturnBlock(
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
