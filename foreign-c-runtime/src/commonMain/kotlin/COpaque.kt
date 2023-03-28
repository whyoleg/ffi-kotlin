package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.memory.access.*
import kotlin.jvm.*

@OptIn(ForeignMemoryApi::class)
public abstract class COpaque
@ForeignMemoryApi
constructor() : EmptyMemoryValue()

@Deprecated("Getting opaque value doesn't make sense")
@get:JvmName("getOpaqueValue")
@set:JvmName("setOpaqueValue")
@OptIn(ForeignMemoryApi::class)
public inline var <KT : COpaque> CPointer<KT>.pointed: KT
    get() = accessor.getRaw(segment)
    set(value) = accessor.setRaw(segment, value)
