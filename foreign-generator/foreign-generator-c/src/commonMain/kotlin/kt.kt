package dev.whyoleg.foreign.generator.c

import dev.whyoleg.foreign.cx.index.*
import dev.whyoleg.foreign.schema.c.*

internal fun CxTypedefInfo.toKotlinDeclaration(
    index: CxIndex,
    visibility: ForeignCDeclaration.Visibility
): String = buildString {
    append(visibility.name)
    append(" typealias ").append(name.value).append(" = ").append(aliased.type.toKotlinType(index))
}

internal fun CxStructInfo.toKotlinDeclaration(
    index: CxIndex,
    visibility: ForeignCDeclaration.Visibility
): String = buildString {
    if (fields.isEmpty()) {
        append(visibility.name)
        append(" class ").append(name.value)
            .append(" private constructor(): COpaque() {")
            .appendLine()
        append(INDENT)
            .append(visibility.name)
            .append(" companion object Type : CType.Opaque").appendAngled(name).append("(").append(name.value).append("())\n}")
    } else {
        append(visibility.name)
        append(" class ").append(name.value)
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
            "${INDENT}${visibility.name} var ${field.name}: ${field.type.type.toKotlinType(index)} by Type.${field.name}"
        }

        append(INDENT)
            .append(visibility.name)
            .append(" companion object Type : CType.Struct").appendAngled(name).append("() {")
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

internal fun CxFunctionInfo.toKotlinExpectDeclaration(
    index: CxIndex,
    visibility: ForeignCDeclaration.Visibility
): String = buildString {
    append(visibility).append(" ")
    append("expect ")
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

internal fun CxType.toKotlinType(index: CxIndex): String = when (this) {
    CxType.Char               -> "Byte"
    CxType.Byte               -> "Byte"
    CxType.UByte              -> "UByte"
    CxType.Short              -> "Short"
    CxType.UShort             -> "UShort"
    CxType.Int                -> "Int"
    CxType.UInt               -> "UInt"
    CxType.Long               -> "PlatformInt"
    CxType.ULong              -> "PlatformUInt"
    CxType.LongLong           -> "Long"
    CxType.ULongLong          -> "ULong"
    CxType.Void               -> "Unit" //TODO

    is CxType.Typedef         -> index.typedef(id).name.value
    is CxType.Struct          -> index.struct(id).name.value
    is CxType.IncompleteArray -> "CArrayPointer<${elementType.toKotlinType(index).replace("?", "")}>?"
    is CxType.ConstArray      -> "CArrayPointer<${elementType.toKotlinType(index).replace("?", "")}>?"
    is CxType.Pointer         -> when (pointed) {
        CxType.Char -> "CString?"
        else        -> "CPointer<${pointed.toKotlinType(index).replace("?", "")}>?"
    }
    else                      -> TODO(toString())
}

internal fun CxType.toKotlinAccessor(index: CxIndex): String = when (this) {
    CxType.Char               -> "Byte"
    CxType.Byte               -> "Byte"
    CxType.UByte              -> "UByte"
    CxType.Short              -> "Short"
    CxType.UShort             -> "UShort"
    CxType.Int                -> "Int"
    CxType.UInt               -> "UInt"
    CxType.Long               -> "PlatformInt"
    CxType.ULong              -> "PlatformUInt"
    CxType.LongLong           -> "Long"
    CxType.ULongLong          -> "ULong"
    CxType.Void               -> "Unit" //TODO

    is CxType.Typedef         -> index.typedef(id).name.value //TODO!!!
    is CxType.Struct          -> index.struct(id).name.value
    is CxType.ConstArray      -> "${elementType.toKotlinAccessor(index)}.pointer"
    is CxType.IncompleteArray -> "${elementType.toKotlinAccessor(index)}.pointer"
    is CxType.Pointer         -> "${pointed.toKotlinAccessor(index)}.pointer"
    else                      -> TODO(toString())
}
