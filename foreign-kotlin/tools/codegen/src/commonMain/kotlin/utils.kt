package dev.whyoleg.foreign.tool.codegen

import dev.whyoleg.foreign.tool.cbridge.api.*

internal fun CType.isVoid(): Boolean = this == CType.Void
