package dev.whyoleg.foreign.memory

public expect abstract class MemoryScope {
    @ForeignMemoryApi
    public fun allocateMemory(layout: MemoryLayout): MemorySegment

    public class Closeable() : MemoryScope, AutoCloseable

    public object Auto : MemoryScope
}

public inline fun <T> memoryScoped(block: MemoryScope.() -> T): T = MemoryScope.Closeable().use(block)
