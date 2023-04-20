package dev.whyoleg.foreign.generator.c

import dev.whyoleg.foreign.cx.index.*

internal fun CxTypedefInfo.toKotlinDeclaration(
    index: CxIndex
): String = buildString {
    append("typealias ").append(name.value).append(" = ").append(aliased.type.toKotlinType(index))
}

internal fun CxStructInfo.toKotlinDeclaration(
    index: CxIndex
): String = buildString {
    if (fields.isEmpty()) {
        append("class ").append(name.value)
            .append(" private constructor(): COpaque() {")
            .appendLine()
        append(INDENT)
            .append("companion object Type : CType.Opaque").appendAngled(name).append("(").append(name.value).append("())\n}")
    } else {
        append("class ").append(name.value)
            .append(" private constructor(segment: MemorySegment): CStruct").appendAngled(name).append("(segment) {")
            .appendLine()
        append(INDENT)
            .append("override val type: CType.Struct").appendAngled(name).append(" get() = Type")
            .appendLine()
        fields.joinTo(
            this,
            prefix = "\n",
            separator = "\n",
            postfix = "\n\n"
        ) { field ->
            "${INDENT}var${field.name}: ${field.type.type.toKotlinType(index)} by Type.${field.name}"
        }

        append(INDENT)
            .append("companion object Type : CType.Struct").appendAngled(name).append("() {")
            .appendLine()
        fields.joinTo(
            this,
            separator = "\n",
            postfix = "\n\n"
        ) { field ->
            "$INDENT${INDENT}private val ${field.name} = element(${field.type.type.toKotlinAccessor(index)})"
        }

        append(INDENT).append(INDENT)
            .append("override val accessor: ValueMemoryAccessor").appendAngled(name).append(" get() = Accessor")
            .appendLine()
            .appendLine()
        append(INDENT).append(INDENT)
            .append("private open class Accessor private constructor(offset: MemoryAddressSize) : ValueMemoryAccessor")
            .appendAngled(name).append("(offset) {")
            .appendLine()

        append(INDENT).append(INDENT).append(INDENT)
            .append("override val layout: MemoryLayout get() = Type.layout")
            .appendLine()
        append(INDENT).append(INDENT).append(INDENT)
            .append("override fun withOffset(offset: MemoryAddressSize): MemoryAccessor").appendAngled(name).append(" = Accessor(offset)")
            .appendLine()
        append(INDENT).append(INDENT).append(INDENT)
            .append("override fun wrap(segment: MemorySegment): ").append(name.value).append(" = ").append(name.value).append("(segment)")
            .appendLine()
            .appendLine()

        append(INDENT).append(INDENT).append(INDENT)
            .append("companion object : Accessor(memoryAddressSizeZero())")
            .appendLine()

        append(INDENT).append(INDENT)
            .append("}")
            .appendLine()
        append(INDENT)
            .append("}")
            .appendLine()

        append("}")
    }
}

//TODO: struct support
internal fun CxFunctionInfo.toKotlinCommonDeclaration(
    index: CxIndex,
    expect: Boolean,
    visibility: Visibility
): String = buildString {
    append(visibility).append(" ")
    if (expect) append("expect ")
    append(toKotlinDeclaration(index))
}

//TODO: struct support
internal fun CxFunctionInfo.toKotlinDeclaration(
    index: CxIndex
): String = buildString {
    append("fun ").append(name.value).append("(")
    if (parameters.isNotEmpty()) parameters.joinTo(
        this,
        prefix = "\n",
        separator = "\n",
        postfix = "\n"
    ) { parameter ->
        "$INDENT${parameter.name}: ${parameter.type.type.toKotlinType(index)},"
    }
    append("): ").append(returnType.type.toKotlinType(index))
}

private fun StringBuilder.appendAngled(name: CxDeclarationName): StringBuilder {
    return append("<").append(name.value).append(">")
}

internal fun CxType.toKotlinType(
    index: CxIndex
): String = when (this) {
//    is CxType.ConstArray      -> TODO()
//    is CxType.IncompleteArray -> TODO()
//    CxType.Boolean            -> TODO()
//    CxType.Byte               -> TODO()
    CxType.Char        -> "Byte"
//    CxType.Double             -> TODO()
//    is CxType.Enum            -> TODO()
//    CxType.Float              -> TODO()
    is CxType.Function -> {
        "CFUNCTION"
//        parameters.joinToString(
//            prefix = "(",
//            separator = ", ",
//            postfix = ") -> ${returnType.toKotlinType(index)}"
//        ) { parameter ->
//            parameter.toKotlinType(index)
//        }
    }
    CxType.Int         -> "Int"
//    CxType.Int128             -> TODO()
//    CxType.Long               -> TODO()
//    CxType.LongDouble         -> TODO()
//    CxType.LongLong           -> TODO()
    is CxType.Pointer  -> when (pointed) {
        CxType.Char -> "CString?"
        else        -> "CPointer<${pointed.toKotlinType(index)}>?"
    }
//    CxType.Short              -> TODO()
    is CxType.Struct   -> index.struct(id).name.value
    is CxType.Typedef  -> index.typedef(id).name.value
//    CxType.UByte              -> TODO()
    CxType.UInt        -> "UInt"
//    CxType.UInt128            -> TODO()
    CxType.ULong       -> "PlatformUInt"
//    CxType.ULongLong          -> TODO()
//    CxType.UShort             -> TODO()
//    is CxType.Unknown         -> TODO()
    CxType.Void        -> "Unit" //TODO
    else               -> TODO(toString())
}

internal fun CxType.toKotlinAccessor(
    index: CxIndex
): String = when (this) {
//    is CxType.ConstArray      -> TODO()
//    is CxType.IncompleteArray -> TODO()
//    CxType.Boolean            -> TODO()
//    CxType.Byte               -> TODO()
    CxType.Char       -> "Byte"
//    CxType.Double             -> TODO()
//    is CxType.Enum            -> TODO()
//    CxType.Float              -> TODO()
//    is CxType.Function        -> TODO()
//    CxType.Int                -> TODO()
//    CxType.Int128             -> TODO()
//    CxType.Long               -> TODO()
//    CxType.LongDouble         -> TODO()
//    CxType.LongLong           -> TODO()
    is CxType.Pointer -> "${pointed.toKotlinAccessor(index)}.pointer"
//    CxType.Short              -> TODO()
//    is CxType.Struct          -> TODO()
    is CxType.Typedef -> index.typedef(id).name.value //TODO!!!
//    CxType.UByte              -> TODO()
    CxType.UInt       -> "UInt"
//    CxType.UInt128            -> TODO()
    CxType.ULong      -> "PlatformUInt"
//    CxType.ULongLong          -> TODO()
//    CxType.UShort             -> TODO()
//    is CxType.Unknown         -> TODO()
    CxType.Void       -> "Unit"
    else              -> TODO(toString())
}
