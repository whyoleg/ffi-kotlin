package dev.whyoleg.foreign.memory

//@Suppress("NO_ACTUAL_FOR_EXPECT")
@ForeignMemoryApi
public expect class MemoryAllocator

@ForeignMemoryApi
public interface MemoryArena : AutoCloseable {
    public val allocator: MemoryAllocator

    public fun allocate(size: MemoryAddressSize, alignment: MemoryAddressSize): MemorySegment

    public fun allocate(layout: MemoryLayout): MemorySegment {
        return allocate(layout.size, layout.alignment)
    }

    public fun allocateArray(elementLayout: MemoryLayout, elementsCount: Int): MemorySegment {
        return allocate(elementLayout.size * elementsCount, elementLayout.alignment)
    }

    public fun allocateString(value: String): MemorySegment

    public fun wrap(address: MemoryAddress, layout: MemoryLayout): MemorySegment?

    //TODO: name, contract?
    public fun invokeOnClose(block: () -> Unit)
}
