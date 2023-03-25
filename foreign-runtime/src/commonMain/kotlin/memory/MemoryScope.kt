package dev.whyoleg.foreign.memory

public sealed class MemoryScope {
    @ForeignMemoryApi
    public abstract fun allocateMemory(layout: MemoryLayout): MemorySegment
}
