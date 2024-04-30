package dev.whyoleg.foreign

public sealed interface MemoryAccess {
    // TODO: by default this should return Arena which will be automatically deallocated when inaccessible
    //  to not leak memory
    //  should be flag/separate functions, etc
    public fun createArena(): MemoryArena
}

// now it's used as receiver, but when `context parameters` will be ready, we should migrate to them
public sealed interface MemoryScope : MemoryAccess {
    public fun Unsafe.allocateCPointer(size: MemorySizeInt, alignment: MemorySizeInt): MemoryBlock
    public fun Unsafe.allocate(layout: MemoryLayout): MemoryBlock
    public fun Unsafe.allocateCArray(elementLayout: MemoryLayout, elementsCount: Int): MemoryBlock
    public fun Unsafe.allocateString(value: String): MemoryBlock

    // TODO: there should be something for FFM
    // TODO: may be somehow improve this...
    public fun <KT> Unsafe.wrapMemoryBlock(
        address: MemorySizeInt,
        layout: MemoryLayout,
        cleanupAction: MemoryCleanupAction<KT>?,
        wrapper: MemoryBlockWrapper<KT>
    ): KT?

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

public fun interface MemoryBlockWrapper<T> {
    public fun wrap(block: MemoryBlock): T
}

public inline fun <T> MemoryAccess.memoryScoped(block: MemoryScope.() -> T): T = createArena().use(block)

// TODO - is it possible to have?
public expect fun AutoMemoryAccess(): MemoryAccess
