package dev.whyoleg.foreign.generator.cx.declarations

import dev.whyoleg.foreign.index.cx.*

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
