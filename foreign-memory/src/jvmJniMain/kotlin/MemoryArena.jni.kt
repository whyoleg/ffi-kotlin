package dev.whyoleg.foreign.memory

@Suppress("ACTUAL_WITHOUT_EXPECT")
@ForeignMemoryApi
public actual typealias MemoryAllocator = BufferAllocator

@ForeignMemoryApi
internal sealed class MemoryArenaImpl : MemoryArena {
    override val allocator: MemoryAllocator get() = BufferAllocator

    override fun wrap(address: MemoryAddress, layout: MemoryLayout): MemorySegment? {
        TODO("Not yet implemented")
    }

    class Shared : MemoryArenaImpl() {
        private val roots = mutableListOf<BufferHolder.Root>()
        override fun allocate(size: MemoryAddressSize, alignment: MemoryAddressSize): MemorySegment {
            val holder = BufferAllocator.allocate(size)
            roots.add(holder)
            return MemorySegment(holder)
        }

        override fun close(): Unit {
            // no real cleanup for ByteBuffers for now
            roots.forEach { it.close() }
            roots.clear()
        }
    }

    object Implicit : MemoryArenaImpl() {
        override fun allocate(size: MemoryAddressSize, alignment: MemoryAddressSize): MemorySegment {
            val holder = BufferAllocator.allocate(size)
            return MemorySegment(holder)
        }

        override fun close() {
            //no-op
        }
    }
}
