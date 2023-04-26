package dev.whyoleg.foreign.generator.c.declarations

import dev.whyoleg.foreign.cx.index.*
import dev.whyoleg.foreign.schema.c.*

internal fun CxFunctionInfo.toKotlinFfmDeclaration(
    index: CxIndex,
    libraryName: String,
    actual: Boolean,
    visibility: ForeignCDeclaration.Visibility
): String = buildString {
    append("private val ").append(prefixedName).append(": MethodHandle = FFI.methodHandle(").appendLine()
    append(INDENT).append("name = \"").append(name.value).append("\",").appendLine()
    if (returnType.type != CxType.Void) append(INDENT).append("result = ").append(returnType.type.toLayout(index)).append(",").appendLine()
    parameters.joinToIfNotEmpty(
        this,
        prefix = "${INDENT}args = arrayOf(\n",
        separator = ",\n",
        postfix = "\n$INDENT)\n"
    ) { paramter ->
        "$INDENT$INDENT${paramter.type.type.toLayout(index)}"
    }
    append(")").appendLine().appendLine()

    append(visibility.name).append(" ")
    if (actual) append("actual ")
    append(toKotlinDeclaration(index, libraryName, !actual)).append(" = ")

    if (returnType.type.isRecord(index)) {
        append("scope.unsafe {").appendLine()
    } else {
        append(libraryName).append("ImplicitScope.unsafe {").appendLine()
    }

    append(INDENT).append("(").append(prefixedName).append(".invokeExact(")
    buildList {
        if (returnType.type.isRecord(index)) {
            add("arena.allocator")
        }
        parameters.forEach { parameter ->
            add("${parameter.name}${parameter.type.type.convertToKotlinFfmParameterType(index)}")
        }
    }.joinToIfNotEmpty(
        this,
        prefix = "\n",
        separator = "\n",
        postfix = "\n$INDENT"
    ) { usage ->
        "$INDENT$INDENT$usage,"
    }
    append(")").append(returnType.type.toFfmReturnTypeCast(index)).append(")")
        .append(returnType.type.convertToKotlinFfmReturnType(index))
        .appendLine()

    append("}")
}

private fun CxType.toLayout(index: CxIndex): String = when (this) {
    CxType.Char               -> "ValueLayout.JAVA_BYTE"
    CxType.Byte               -> "ValueLayout.JAVA_BYTE"
    CxType.UByte              -> "ValueLayout.JAVA_BYTE"
    CxType.Short              -> "ValueLayout.JAVA_SHORT"
    CxType.UShort             -> "ValueLayout.JAVA_SHORT"
    CxType.Int                -> "ValueLayout.JAVA_INT"
    CxType.UInt               -> "ValueLayout.JAVA_INT"
    CxType.Long               -> "ValueLayout.JAVA_LONG" //TODO
    CxType.ULong              -> "ValueLayout.JAVA_LONG"
    CxType.LongLong           -> "ValueLayout.JAVA_LONG"
    CxType.ULongLong          -> "ValueLayout.JAVA_LONG"
    CxType.Float              -> "ValueLayout.JAVA_FLOAT"
    CxType.Double             -> "ValueLayout.JAVA_DOUBLE"
//    CxType.Void               -> "Unit" //TODO

    is CxType.Typedef         -> index.typedef(id).aliased.type.toLayout(index)
    is CxType.Record          -> index.record(id).name!!.value + ".layout" //TODO
    is CxType.Enum            -> "ValueLayout.JAVA_INT" // enum is represented as Int value //TODO or long?
    is CxType.ConstArray      -> "ValueLayout.ADDRESS"
    is CxType.IncompleteArray -> "ValueLayout.ADDRESS"
    is CxType.Pointer         -> "ValueLayout.ADDRESS"
    else                      -> TODO(toString())
}


private fun CxType.toFfmReturnTypeCast(index: CxIndex): String = when (this) {
    CxType.Char               -> " as Byte"
    CxType.Byte               -> " as Byte"
    CxType.UByte              -> " as Byte"
    CxType.Short              -> " as Short"
    CxType.UShort             -> " as Short"
    CxType.Int                -> " as Int"
    CxType.UInt               -> " as Int"
    CxType.Long               -> " as Long"
    CxType.ULong              -> " as Long"
    CxType.LongLong           -> " as Long"
    CxType.ULongLong          -> " as Long"
    CxType.Float              -> " as Float"
    CxType.Double             -> " as Double"
    CxType.Void               -> ""

    is CxType.Typedef         -> index.typedef(id).aliased.type.toFfmReturnTypeCast(index)
    is CxType.Record          -> " as MemorySegment" // we return it via a pointer
    is CxType.Enum            -> " as Int" // enum is represented as Int value
    is CxType.ConstArray      -> " as MemorySegment"
    is CxType.IncompleteArray -> " as MemorySegment"
    is CxType.Pointer         -> " as MemorySegment"
    else                      -> TODO(toString())
}

private fun CxType.convertToKotlinFfmReturnType(index: CxIndex): String = when (this) {
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

    is CxType.Typedef         -> index.typedef(id).aliased.type.convertToKotlinFfmReturnType(index)
    is CxType.Record          -> ".also { address -> CGrouped(${index.record(id).name!!.value}, address) }"
    is CxType.Enum            -> ".also(::${index.enum(id).name!!}.Value)"
    //TODO: Array types?
    is CxType.ConstArray      -> ".also { address -> CPointer(${elementType.toKotlinType(index)}, address) }"
    is CxType.IncompleteArray -> ".also { address -> CPointer(${elementType.toKotlinType(index)}, address) }"
    is CxType.Pointer         -> ".also { address -> CPointer(${pointed.toKotlinType(index)}, address) }"
    else                      -> TODO(toString())
}

private fun CxType.convertToKotlinFfmParameterType(index: CxIndex): String = when (this) {
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

    is CxType.Typedef         -> index.typedef(id).aliased.type.convertToKotlinFfmParameterType(index)
    is CxType.Enum            -> ".underlying"
    is CxType.ConstArray      -> ".address"
    is CxType.IncompleteArray -> ".address"
    is CxType.Pointer         -> ".address"
    else                      -> TODO(toString())
}
