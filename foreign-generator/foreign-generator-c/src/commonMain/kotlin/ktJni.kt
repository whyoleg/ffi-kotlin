package dev.whyoleg.foreign.generator.c

import dev.whyoleg.foreign.cx.index.*

internal fun CxFunctionInfo.toKotlinJniDeclaration(
    index: CxIndex,
    libraryName: String,
    actual: Boolean,
    visibility: Visibility
): String = buildString {
    append("@JvmStatic private external fun ").append(prefixedName).append("(")
    if (parameters.isNotEmpty()) parameters.joinTo(
        this,
        prefix = "\n",
        separator = "\n",
        postfix = "\n"
    ) { parameter ->
        "${INDENT}p_${parameter.name}: ${parameter.type.type.toKotlinJniType()},"
    }
    append("): ").append(returnType.type.toKotlinJniType())
        .appendLine()
        .appendLine()

    append(visibility.name).append(" ")
    if (actual) append("actual ")
    append(toKotlinDeclaration(index)).append(" = ").append(libraryName).append("ImplicitScope.unsafe {")
        .appendLine()

    append(INDENT).append(prefixedName).append("(")
    if (parameters.isNotEmpty()) parameters.joinTo(
        this,
        prefix = "\n",
        separator = "\n",
        postfix = "\n$INDENT"
    ) { parameter ->
        "$INDENT${INDENT}p_${parameter.name} = ${parameter.name}${parameter.type.type.convertToKotlinJniParameterType(index)},"
    }
    append(")").append(returnType.type.convertToKotlinJniReturnType(index))
        .appendLine()
    append("}")
}


private fun CxType.toKotlinJniType(): String = when (this) {
//    is CxType.ConstArray      -> TODO()
//    is CxType.IncompleteArray -> TODO()
//    CxType.Boolean            -> TODO()
//    CxType.Byte               -> TODO()
//    CxType.Char               -> TODO()
//    CxType.Double             -> TODO()
//    is CxType.Enum            -> TODO()
//    CxType.Float              -> TODO()
//    is CxType.Function        -> TODO()
//    CxType.Int                -> TODO()
//    CxType.Int128             -> TODO()
//    CxType.Long               -> TODO()
//    CxType.LongDouble         -> TODO()
//    CxType.LongLong           -> TODO()
    is CxType.Pointer -> "Long"
//    CxType.Short              -> TODO()
//    is CxType.Struct          -> TODO()
//    is CxType.Typedef         -> TODO()
//    CxType.UByte              -> TODO()
//    CxType.UInt               -> TODO()
//    CxType.UInt128            -> TODO()
    CxType.ULong      -> "Long"
//    CxType.ULongLong          -> TODO()
//    CxType.UShort             -> TODO()
//    is CxType.Unknown         -> TODO()
    CxType.Void       -> "Unit"
    else              -> TODO(toString())
}

private fun CxType.convertToKotlinJniReturnType(
    index: CxIndex
): String = when (this) {
//    is CxType.ConstArray      -> TODO()
//    is CxType.IncompleteArray -> TODO()
//    CxType.Boolean            -> TODO()
//    CxType.Byte               -> TODO()
//    CxType.Char               -> TODO()
//    CxType.Double             -> TODO()
//    is CxType.Enum            -> TODO()
//    CxType.Float              -> TODO()
//    is CxType.Function        -> TODO()
//    CxType.Int                -> TODO()
//    CxType.Int128             -> TODO()
//    CxType.Long               -> TODO()
//    CxType.LongDouble         -> TODO()
//    CxType.LongLong           -> TODO()
    is CxType.Pointer -> ".also { address -> CPointer(${pointed.toKotlinType(index)}, address) }"
//    CxType.Short              -> TODO()
//    is CxType.Struct          -> TODO()
//    is CxType.Typedef         -> TODO()
//    CxType.UByte              -> TODO()
//    CxType.UInt               -> TODO()
//    CxType.UInt128            -> TODO()
    CxType.ULong      -> ".toPlatformUInt()"
//    CxType.ULongLong          -> TODO()
//    CxType.UShort             -> TODO()
//    is CxType.Unknown         -> TODO()
//    CxType.Void       -> "Unit"
    else              -> TODO(toString())
}

private fun CxType.convertToKotlinJniParameterType(
    index: CxIndex
): String = when (this) {
//    is CxType.ConstArray      -> TODO()
//    is CxType.IncompleteArray -> TODO()
//    CxType.Boolean            -> TODO()
//    CxType.Byte               -> TODO()
//    CxType.Char               -> TODO()
//    CxType.Double             -> TODO()
//    is CxType.Enum            -> TODO()
//    CxType.Float              -> TODO()
//    is CxType.Function        -> TODO()
//    CxType.Int                -> TODO()
//    CxType.Int128             -> TODO()
//    CxType.Long               -> TODO()
//    CxType.LongDouble         -> TODO()
//    CxType.LongLong           -> TODO()
    is CxType.Pointer -> ".address"
//    CxType.Short              -> TODO()
//    is CxType.Struct          -> TODO()
//    is CxType.Typedef         -> TODO()
//    CxType.UByte              -> TODO()
//    CxType.UInt               -> TODO()
//    CxType.UInt128            -> TODO()
    CxType.ULong      -> ".toLong()"
//    CxType.ULongLong          -> TODO()
//    CxType.UShort             -> TODO()
//    is CxType.Unknown         -> TODO()
//    CxType.Void       -> "Unit"
    else              -> TODO(toString())
}
