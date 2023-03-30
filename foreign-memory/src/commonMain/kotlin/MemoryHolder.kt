@file:OptIn(ForeignMemoryApi::class)

package dev.whyoleg.foreign.memory

public sealed class MemoryHolder(
    protected val segment: MemorySegment,
) {
    public val isAccessible: Boolean get() = segment.isAccessible

    @PublishedApi
    @ForeignMemoryApi
    internal val segmentInternal: MemorySegment get() = segment
}

public abstract class MemoryReference @ForeignMemoryApi constructor(segment: MemorySegment) : MemoryHolder(segment)

//TODO: other name?
public abstract class MemoryValue @ForeignMemoryApi constructor(segment: MemorySegment) : MemoryHolder(segment)

//for opaque support
public abstract class EmptyMemoryValue @ForeignMemoryApi constructor()
