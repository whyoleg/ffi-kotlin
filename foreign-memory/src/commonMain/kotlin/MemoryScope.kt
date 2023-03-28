package dev.whyoleg.foreign.memory

public expect abstract class MemoryScope {
    @ForeignMemoryApi
    public abstract fun allocateMemory(layout: MemoryLayout): MemorySegment

    //TODO: add AutoCloseable, because now there is an error on jvm...
    public class Closeable() : MemoryScope {
        public fun close()
    }

    public object Auto : MemoryScope
}

public inline fun <T> memoryScoped(block: MemoryScope.() -> T): T {
    //return MemoryScope.Closeable().use(block)
    val scope = MemoryScope.Closeable()
    try {
        return scope.block()
    } finally {
        scope.close()
    }
}
