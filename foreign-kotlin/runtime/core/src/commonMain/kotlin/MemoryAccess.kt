package dev.whyoleg.foreign

public object UnsafeMemoryAccess

public sealed interface MemoryAccess {
    // TODO: by default this should return Arena which will be automatically deallocated when inaccessible
    //  to not leak memory
    //  should be flag/separate functions, etc
    public fun createArena(): MemoryArena
}

public sealed interface MemoryScope : MemoryAccess {
    public fun UnsafeMemoryAccess.allocate(size: MemorySizeInt, alignment: MemorySizeInt): MemoryBlock
    public fun UnsafeMemoryAccess.allocate(layout: MemoryLayout): MemoryBlock
    public fun UnsafeMemoryAccess.allocateArray(elementLayout: MemoryLayout, elementsCount: Int): MemoryBlock
    public fun UnsafeMemoryAccess.allocateString(value: String): MemoryBlock

    // public fun wrap(address: MemorySizeInt, layout: MemoryLayout): MemoryBlock?
}

public sealed interface MemoryArena : MemoryScope, AutoCloseable {
    //TODO: name, contract?
    // public fun invokeOnClose(block: () -> Unit)
    // public fun defer(block: () -> Unit)
}

public fun interface MemoryCleanupAction<T> {
    public fun cleanup(value: T)
}

public inline fun <T> MemoryAccess.memoryScoped(block: MemoryScope.() -> T): T = createArena().use(block)

// TODO - is it possible to have?
public expect fun AutoMemoryAccess(): MemoryAccess
