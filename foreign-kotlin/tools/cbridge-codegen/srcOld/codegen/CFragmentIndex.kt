package dev.whyoleg.foreign.codegen

import dev.whyoleg.foreign.bridge.c.*

internal class CFragmentIndex(
    val enums: Map<CDeclarationId, CEnum>,
    val typedefs: Map<CDeclarationId, CTypedef>,
    val records: Map<CDeclarationId, CRecord>,
)

// if shared - generate expects
// if not shared - generate actual declarations or just declarations if it's not available in shared

// TODO: those are mostly for return type of function - refactor

// TODO: better name
internal fun CType.requiresPointerAllocation(index: CFragmentIndex): Boolean = when (this) {
    is CType.Typedef     -> index.typedefs.getValue(id).resolvedType.requiresPointerAllocation(index)

    is CType.Record,
    is CType.Pointer,
    is CType.Array       -> true

    CType.Void,
    CType.Boolean,
    is CType.Number,
    is CType.Enum,
    is CType.Function,
    is CType.Mixed,
    is CType.Unsupported -> false
}

internal fun CType.isPointerLike(index: CFragmentIndex): Boolean = when (this) {
    is CType.Typedef     -> index.typedefs.getValue(id).resolvedType.isPointerLike(index)

    is CType.Pointer,
    is CType.Array       -> true

    CType.Void,
    CType.Boolean,
    is CType.Number,
    is CType.Enum,
    is CType.Record,
    is CType.Function,
    is CType.Mixed,
    is CType.Unsupported -> false
}

internal fun CType.isVoid(): Boolean = this == CType.Void

internal fun CType.asPointer(index: CFragmentIndex): CType.Pointer? = when (this) {
    is CType.Typedef -> index.typedefs.getValue(id).resolvedType.asPointer(index)
    is CType.Pointer -> this
    else             -> null
}

internal fun CType.isRecord(index: CFragmentIndex): Boolean = when (this) {
    is CType.Typedef -> index.typedefs.getValue(id).resolvedType.isRecord(index)
    is CType.Record  -> true
    else             -> false
}

internal fun CType.resolvedType(index: CFragmentIndex): CType = when (this) {
    is CType.Typedef -> index.typedefs.getValue(id).resolvedType
    else             -> this
}