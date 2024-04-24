package dev.whyoleg.foreign.cx.bindings.generator.declarations

import dev.whyoleg.foreign.cx.index.*
import dev.whyoleg.foreign.cx.metadata.*

internal fun CxFunctionInfo.toKotlinNativeDeclaration(
    index: CxIndex,
    libraryName: String,
    actual: Boolean,
    visibility: ForeignCDeclaration.Visibility
): String = buildString {
    append("@CCall(\"").append(prefixedName).append("\") private external fun ").append(prefixedName).append("(")
    parametersWithReturnType(index).joinToIfNotEmpty(
        this,
        prefix = "\n",
        separator = "\n",
        postfix = "\n"
    ) { parameter ->
        "${INDENT}p_${parameter.name}: ${parameter.type.type.toKotlinNativeType(index)}"
    }
    append("): ").append(returnType.type.toKotlinNativeType(index))
        .appendLine()
        .appendLine()

    append(visibility.name).append(" ")
    if (actual) append("actual ")
    append(toKotlinDeclaration(index, libraryName, !actual)).append(" = ")

    fun appendNativeCall(indent: String) {
        append(indent).append(prefixedName).append("(")
        buildList {
            parameters.forEach { parameter ->
                add("p_${parameter.name} = ${parameter.name}${parameter.type.type.convertToKotlinNativeParameterType(index)}")
            }
            if (returnType.type.isRecord(index)) {
                add("p_return_pointer = return_pointer")
            }
        }.joinToIfNotEmpty(
            this,
            prefix = "\n",
            separator = "\n",
            postfix = "\n$indent"
        ) { usage ->
            "$indent$INDENT$usage,"
        }
        append(")").append(returnType.type.convertToKotlinNativeReturnType(index))
            .appendLine()
    }

    if (returnType.type.isRecord(index)) {
        append("scope.unsafe {").appendLine()
        append(INDENT)
            .append("CRecord(").append(returnType.type.toKotlinType(index)).append(") { return_pointer ->")
            .appendLine()
        appendNativeCall(INDENT + INDENT)
        append(INDENT)
            .append("}")
            .appendLine()
    } else {
        append(libraryName).append("ImplicitScope.unsafe {").appendLine()
        appendNativeCall(INDENT)
    }
    append("}")
}

private fun CxType.toKotlinNativeType(index: CxIndex): String = when (this) {
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
    CxType.Float              -> "Float"
    CxType.Double             -> "Double"
    CxType.Void               -> "Unit" //TODO

    is CxType.Typedef         -> index.typedef(id).aliased.type.toKotlinNativeType(index)
    is CxType.Record          -> "Unit" // we return it via a pointer
    is CxType.Enum            -> "Int" // enum is represented as Int value
    is CxType.ConstArray      -> "Long"
    is CxType.IncompleteArray -> "Long"
    is CxType.Pointer         -> "Long"
    else                      -> TODO(toString())
}

private fun CxType.convertToKotlinNativeReturnType(index: CxIndex): String = when (this) {
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
    CxType.Float              -> ""
    CxType.Double             -> ""
    CxType.Void               -> ""

    is CxType.Typedef         -> index.typedef(id).aliased.type.convertToKotlinNativeReturnType(index)
    is CxType.Record          -> ""
    is CxType.Enum            -> ".also(::${index.enum(id).name!!}.Value)"
    //TODO: Array types?
    is CxType.ConstArray      -> ".also { address -> CPointer(${elementType.toKotlinType(index)}, address) }"
    is CxType.IncompleteArray -> ".also { address -> CPointer(${elementType.toKotlinType(index)}, address) }"
    is CxType.Pointer         -> ".also { address -> CPointer(${pointed.toKotlinType(index)}, address) }"
    else                      -> TODO(toString())
}

private fun CxType.convertToKotlinNativeParameterType(index: CxIndex): String = when (this) {
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
    CxType.Float              -> ""
    CxType.Double             -> ""

    is CxType.Typedef         -> index.typedef(id).aliased.type.convertToKotlinNativeParameterType(index)
    is CxType.Enum            -> ".underlying"
    is CxType.ConstArray      -> ".address"
    is CxType.IncompleteArray -> ".address"
    is CxType.Pointer         -> ".address"
    else                      -> TODO(toString())
}
