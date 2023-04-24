package dev.whyoleg.foreign.generator.c

import dev.whyoleg.foreign.cx.index.*

internal const val INDENT = "    "
internal const val PREFIX = "foreign_"

internal val CxDeclarationInfo.prefixedName get() = "$PREFIX${name!!.value}"
