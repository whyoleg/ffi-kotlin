package dev.whyoleg.foreign.memory

public abstract class MemoryHolder
@ForeignMemoryApi
constructor(
    public val segment: MemorySegment,
)
