package dev.whyoleg.foreign.tooling.codegen

import dev.whyoleg.foreign.tooling.cbridge.*

internal fun CType.isVoid(): Boolean = this == CType.Void
