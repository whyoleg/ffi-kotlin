package dev.whyoleg.foreign.generator.c.declarations

import dev.whyoleg.foreign.cx.index.*
import dev.whyoleg.foreign.schema.c.*

private const val ANONYMOUS = "anonymous"

internal fun CxRecordInfo.toKotlinDeclaration(
    index: CxIndex,
    visibility: ForeignCDeclaration.Visibility,
    name: CxDeclarationName
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
        val parentType = when (isUnion) {
            true -> "Union"
            else -> "Struct"
        }

        append(visibility.name)
        append(" class ").append(name.value)
            .append(" private constructor(segment: MemorySegment): C").append(parentType).appendAngled(name).append("(segment) {")
            .appendLine()
        append(INDENT)
            .append("override val type: CType.").append(parentType).appendAngled(name).append(" get() = Type")
            .appendLine()
        fields.joinTo(
            this,
            prefix = "\n",
            separator = "\n",
            postfix = "\n\n"
        ) { field ->
            "$INDENT${visibility.name} var ${field.name}: ${
                field.type.type.toKotlinTypeOrElse(index, "$ANONYMOUS.${field.name}")
            } by Type.${field.name}"
        }

        append(INDENT)
            .append(visibility.name)
            .append(" companion object Type : CType.").append(parentType).appendAngled(name).append("() {")
            .appendLine()
        fields.joinTo(
            this,
            separator = "\n",
            postfix = "\n\n"
        ) { field ->
            "$INDENT${INDENT}private val ${field.name} = element(${
                field.type.type.toKotlinAccessorOrElse(index, "$ANONYMOUS.${field.name}")
            })"
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

        val anonymousRecords = fields.mapNotNull {
            val type = it.type.type
            if (type !is CxType.Record) return@mapNotNull null
            val record = index.record(type.id)
            if (record.name != null) return@mapNotNull null
            it.name to record
        }

        //not really tested :)
        val anonymousEnums = fields.mapNotNull {
            val type = it.type.type
            if (type !is CxType.Enum) return@mapNotNull null
            val enum = index.enum(type.id)
            if (enum.name != null) return@mapNotNull null
            it.name to enum
        }

        if (anonymousRecords.isNotEmpty() || anonymousEnums.isNotEmpty()) {
            appendLine()
            append(INDENT)
                .append(visibility.name)
                .append(" object ").append(ANONYMOUS).append(" {").appendLine()

            anonymousRecords.forEach { (fieldName, record) ->
                appendLine()
                record.toKotlinDeclaration(index, visibility, CxDeclarationName(fieldName)).split("\n").forEach { line ->
                    if (line.isBlank()) appendLine()
                    else append(INDENT).append(INDENT).append(line).appendLine()
                }
            }

            anonymousEnums.forEach { (fieldName, enum) ->
                appendLine()
                enum.toKotlinDeclaration(visibility, CxDeclarationName(fieldName)).split("\n").forEach { line ->
                    if (line.isBlank()) appendLine()
                    else append(INDENT).append(INDENT).append(line).appendLine()
                }
            }

            append(INDENT)
                .append("}")
                .appendLine()
        }

        append("}")
    }
}

private fun StringBuilder.appendAngled(name: CxDeclarationName): StringBuilder {
    return append("<").append(name.value).append(">")
}

private fun CxType.toKotlinAccessor(index: CxIndex): String {
    return checkNotNull(toKotlinAccessorOrNull(index)) { "type can not be null here: $this" }
}

private fun CxType.toKotlinAccessorOrElse(index: CxIndex, value: String): String {
    return toKotlinAccessorOrNull(index) ?: value
}

private fun CxType.toKotlinAccessorOrNull(index: CxIndex): String? = when (this) {
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
    CxType.Float              -> "Float"
    CxType.Double             -> "Double"
    CxType.Void               -> "Unit"

    is CxType.Typedef         -> index.typedef(id).aliased.type.toKotlinAccessor(index)
    is CxType.Record          -> index.record(id).name?.value
//    is CxType.Enum            -> "ENUM" //TODO: support accessors
    is CxType.ConstArray      -> "${elementType.toKotlinAccessor(index)}.pointer"
    is CxType.IncompleteArray -> "${elementType.toKotlinAccessor(index)}.pointer"
    is CxType.Pointer         -> "${pointed.toKotlinAccessor(index)}.pointer"

    else                      -> TODO(toString())
}
