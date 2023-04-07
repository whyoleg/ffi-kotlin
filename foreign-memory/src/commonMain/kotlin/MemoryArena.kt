package dev.whyoleg.foreign.memory

//@Suppress("NO_ACTUAL_FOR_EXPECT")
@ForeignMemoryApi
public expect class MemoryAllocator

@ForeignMemoryApi
public interface MemoryArena : AutoCloseable {
    public val allocator: MemoryAllocator

    public fun allocate(size: MemoryAddressSize, alignment: MemoryAddressSize): MemorySegment
    public fun allocate(layout: MemoryLayout): MemorySegment = allocate(layout.size, layout.alignment)
    public fun allocateArray(elementLayout: MemoryLayout, elementsCount: Int): MemorySegment = TODO()
    public fun allocateString(value: String): MemorySegment = TODO()

    public fun wrap(address: MemoryAddress, layout: MemoryLayout): MemorySegment?
}
