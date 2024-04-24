@file:Suppress("NOTHING_TO_INLINE")

package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.*

public sealed interface CRecord

public interface CStruct : CRecord
public interface CUnion : CRecord

public interface COpaque : CRecord

public inline fun <KT : CRecord> MemoryScope.allocateFrom(value: KT): CPointer<KT> = TODO()

@Deprecated("Getting opaque value doesn't make sense")
public inline var <KT : COpaque> CPointer<KT>.value: KT
    get() = mapper.getValue(block, offset)
    set(value) = mapper.setValue(block, offset, value)

@Deprecated("Allocating opaque with value doesn't make sense")
public inline fun <KT : COpaque> MemoryScope.allocateFrom(type: CType<KT>, value: KT): CPointer<KT> = TODO()

@Deprecated("Allocating opaque with value doesn't make sense")
public inline fun <KT : COpaque> MemoryScope.allocateFrom(value: KT): CPointer<KT> = TODO()

// TODO: Deprecated array from opaque
