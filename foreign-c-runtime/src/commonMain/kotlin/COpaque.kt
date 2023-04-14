@file:OptIn(ForeignMemoryApi::class)

package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.memory.access.*
import kotlin.jvm.*

public abstract class COpaque @ForeignMemoryApi constructor() : EmptyMemoryValue()

@Deprecated("Getting opaque value doesn't make sense")
@get:JvmName("getOpaqueValue")
@set:JvmName("setOpaqueValue")
public inline var <KT : COpaque> CPointer<KT>.pointed: KT
    get() = accessor.getRaw(segmentInternal2)
    set(value) = accessor.setRaw(segmentInternal2, value)
