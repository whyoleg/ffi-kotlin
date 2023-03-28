package dev.whyoleg.foreign.memory

public expect abstract class MemoryScope {
    @ForeignMemoryApi
    public abstract fun allocateMemory(layout: MemoryLayout): MemorySegment

    //TODO: add AutoCloseable later, because now there is an error on jvm when using it...
    public class Closeable : MemoryScope {
        public fun close()
    }
}

public inline fun <T> memoryScoped(obj: MemoryObject = MemoryObject.Default, block: MemoryScope.() -> T): T {
    //return obj.createScope().use(block)
    val scope = obj.createScope()
    try {
        return scope.block()
    } finally {
        scope.close()
    }
}
