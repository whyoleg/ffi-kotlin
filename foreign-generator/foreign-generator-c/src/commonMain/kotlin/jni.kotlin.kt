package dev.whyoleg.foreign.generator.c

import dev.whyoleg.foreign.cx.index.*
import dev.whyoleg.foreign.schema.c.*

//TODO: struct support
internal fun CxFunctionInfo.toKotlinJniDeclaration(
    index: CxIndex,
    libraryName: String,
    actual: Boolean,
    visibility: ForeignCDeclaration.Visibility
): String = buildString {
    append("@JvmStatic private external fun ").append(prefixedName).append("(")
    if (parameters.isNotEmpty()) parameters.joinTo(
        this,
        prefix = "\n",
        separator = "\n",
        postfix = "\n"
    ) { parameter ->
        "${INDENT}p_${parameter.name}: ${parameter.type.type.toKotlinJniType(index)},"
    }
    append("): ").append(returnType.type.toKotlinJniType(index))
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

private fun CxType.toKotlinJniType(index: CxIndex): String = when (this) {
    CxType.Char               -> "Byte"
    CxType.Byte               -> "Byte"
    CxType.UByte              -> "Byte"
    CxType.Short              -> "Short"
    CxType.UShort             -> "Short"
    CxType.Int                -> "Int"
    CxType.UInt               -> "Int"
    CxType.Long               -> "PlatformInt"
    CxType.ULong              -> "PlatformInt"
    CxType.LongLong           -> "Long"
    CxType.ULongLong          -> "Long"
    CxType.Void               -> "Unit" //TODO

    is CxType.Typedef         -> index.typedef(id).aliased.type.toKotlinJniType(index)
//    is CxType.Struct          -> index.struct(id).name.value
    is CxType.ConstArray      -> "Long"
    is CxType.IncompleteArray -> "Long"
    is CxType.Pointer         -> "Long"
    else                      -> TODO(toString())
}

private fun CxType.convertToKotlinJniReturnType(index: CxIndex): String = when (this) {
    CxType.Char               -> ""
    CxType.Byte               -> ""
    CxType.UByte              -> ".toUByte()"
    CxType.Short              -> ""
    CxType.UShort             -> ".toUShort()"
    CxType.Int                -> ""
    CxType.UInt               -> ".toUInt()"
    CxType.Long               -> ""
    CxType.ULong              -> ".toPlatformUInt()"
    CxType.LongLong           -> ""
    CxType.ULongLong          -> ".toULong()"
    CxType.Void               -> ""

    is CxType.Typedef         -> index.typedef(id).aliased.type.convertToKotlinJniReturnType(index)
//    is CxType.Struct          -> index.struct(id).name.value
    //TODO: Array types?
    is CxType.ConstArray      -> ".also { address -> CPointer(${elementType.toKotlinType(index)}, address) }"
    is CxType.IncompleteArray -> ".also { address -> CPointer(${elementType.toKotlinType(index)}, address) }"
    is CxType.Pointer         -> ".also { address -> CPointer(${pointed.toKotlinType(index)}, address) }"
    else                      -> TODO(toString())
}

private fun CxType.convertToKotlinJniParameterType(index: CxIndex): String = when (this) {
    CxType.Char               -> ""
    CxType.Byte               -> ""
    CxType.UByte              -> ".toByte()"
    CxType.Short              -> ""
    CxType.UShort             -> ".toShort()"
    CxType.Int                -> ""
    CxType.UInt               -> ".toInt()"
    CxType.Long               -> ""
    CxType.ULong              -> ".toPlatformInt()"
    CxType.LongLong           -> ""
    CxType.ULongLong          -> ".toLong()"
//    CxType.Void               -> "" //TODO?

    is CxType.Typedef         -> index.typedef(id).aliased.type.convertToKotlinJniParameterType(index)
//    is CxType.Struct          -> index.struct(id).name.value

    is CxType.ConstArray      -> ".address"
    is CxType.IncompleteArray -> ".address"
    is CxType.Pointer         -> ".address"
    else                      -> TODO(toString())
}
