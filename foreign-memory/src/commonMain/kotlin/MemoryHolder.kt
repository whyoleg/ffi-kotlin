package dev.whyoleg.foreign.memory

public sealed class MemoryHolder
@ForeignMemoryApi
constructor(
    protected val segment: MemorySegment,
) {
    @OptIn(ForeignMemoryApi::class)
    public val isAccessible: Boolean get() = segment.isAccessible

    @PublishedApi
    @ForeignMemoryApi
    internal val segmentInternal: MemorySegment get() = segment
}

@OptIn(ForeignMemoryApi::class)
public abstract class MemoryReference
@ForeignMemoryApi
constructor(
    segment: MemorySegment,
) : MemoryHolder(segment)

//TODO: other name?
@OptIn(ForeignMemoryApi::class)
public abstract class MemoryValue
@ForeignMemoryApi
constructor(
    segment: MemorySegment,
) : MemoryHolder(segment)

//for opaque support
public abstract class EmptyMemoryValue
@ForeignMemoryApi
constructor()
