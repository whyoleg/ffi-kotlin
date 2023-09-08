package dev.whyoleg.foreign.cx.bindings.generator.declarations

import dev.whyoleg.foreign.cx.index.*

internal fun CxType.toKotlinType(index: CxIndex): String {
    return checkNotNull(toKotlinTypeOrNull(index)) { "type can not be null here: $this" }
}

internal fun CxType.toKotlinTypeOrElse(index: CxIndex, value: String): String {
    return toKotlinTypeOrNull(index) ?: value
}

private fun CxType.toKotlinTypeOrNull(index: CxIndex): String? = when (this) {
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

    is CxType.Typedef         -> index.typedef(id).name.value
    is CxType.Record          -> index.record(id).name?.value
    is CxType.Enum            -> "${index.enum(id).name!!.value}.Value"
    is CxType.IncompleteArray -> "CArrayPointer<${elementType.toKotlinType(index).replace("?", "")}>?"
    is CxType.ConstArray      -> "CArrayPointer<${elementType.toKotlinType(index).replace("?", "")}>?"
    is CxType.Pointer         -> when (pointed) {
        CxType.Char -> "CString?"
        else        -> "CPointer<${pointed.toKotlinType(index).replace("?", "")}>?"
    }

    else                      -> TODO(toString())
}
