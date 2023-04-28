package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.memory.access.*
import kotlin.jvm.*

@OptIn(ForeignMemoryApi::class)
public abstract class COpaque @ForeignMemoryApi constructor() : EmptyMemoryValue()

@OptIn(ForeignMemoryApi::class)
@Deprecated("Getting opaque value doesn't make sense")
@get:JvmName("getOpaqueValue")
@set:JvmName("setOpaqueValue")
public inline var <KT : COpaque> CPointer<KT>.pointed: KT
    get() = accessor.getRaw(blockInternalC)
    set(value) = accessor.setRaw(blockInternalC, value)
