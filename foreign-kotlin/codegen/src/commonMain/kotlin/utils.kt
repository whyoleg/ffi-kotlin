package dev.whyoleg.foreign.codegen

import dev.whyoleg.foreign.bridge.c.*


// TODO: better name
internal val CType.returnsPointer: Boolean
    get() = when (this) {
        is CType.Record,
        is CType.Pointer,
        is CType.Array    -> true

        is CType.Typedef  -> TODO()

        is CType.Function -> TODO()
        else              -> false
    }

internal val CType.isPointerLike: Boolean
    get() = when (this) {
        is CType.Pointer,
        is CType.Array    -> true

        is CType.Typedef  -> TODO()
        is CType.Function -> TODO()
        else              -> false
    }

internal val CType.isVoid: Boolean get() = this == CType.Void
