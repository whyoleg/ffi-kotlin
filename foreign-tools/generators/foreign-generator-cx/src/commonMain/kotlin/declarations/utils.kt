package dev.whyoleg.foreign.generator.cx.declarations

import dev.whyoleg.foreign.index.cx.*

internal const val INDENT = "    "
internal const val PREFIX = "foreign_"

internal val CxDeclarationInfo.prefixedName get() = "$PREFIX${name!!.value}"

internal fun CxType.isRecord(index: CxIndex): Boolean = when (this) {
    is CxType.Typedef -> index.typedef(id).aliased.type.isRecord(index)
    is CxType.Record  -> true
    else              -> false
}