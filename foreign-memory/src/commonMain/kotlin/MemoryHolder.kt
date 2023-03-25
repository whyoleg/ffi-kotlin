package dev.whyoleg.foreign.memory

//in bytes
public typealias MemoryAddressSize = Int

public abstract class MemoryHolder
@ForeignMemoryApi
constructor(
    public val segment: MemorySegment,
)
