package dev.whyoleg.foreign.memory

// TODO: decide on naming
public expect class MemoryObject {
    public val autoAllocator: MemoryAllocator
    public fun createScope(): MemoryScope

    public companion object {
        public val Default: MemoryObject
    }
}

public fun interface MemoryAllocator {
    @ForeignMemoryApi
    public fun allocateMemory(layout: MemoryLayout): MemorySegment
}

//TODO: add AutoCloseable later, because now there is an error on jvm when using it...
public interface MemoryScope : MemoryAllocator {
    public fun close()
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
