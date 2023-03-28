package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import kotlin.jvm.*

public abstract class COpaque
@ForeignMemoryApi
constructor()

@Deprecated("Getting opaque value doesn't make sense")
@get:JvmName("getOpaqueValue")
@set:JvmName("setOpaqueValue")
@OptIn(ForeignMemoryApi::class)
public inline var <KT : COpaque> CPointer<KT>.pointed: KT?
    get() = accessor.get(segment)
    set(value) = accessor.set(segment, value)
