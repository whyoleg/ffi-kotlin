package dev.whyoleg.foreign.memory

public sealed class MemoryHolder
@ForeignMemoryApi
constructor(
    public val segment: MemorySegment,
) {
    @OptIn(ForeignMemoryApi::class)
    public val isAccessible: Boolean get() = segment.isAccessible
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
