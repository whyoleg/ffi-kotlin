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
        //TODO: cleaner support?
        private val roots = mutableListOf<BufferHolder.Root>()
        private val actions = mutableListOf<() -> Unit>()
        override fun allocate(size: MemoryAddressSize, alignment: MemoryAddressSize): MemorySegment {
            val holder = BufferAllocator.allocate(size)
            roots.add(holder)
            return MemorySegment(holder)
        }

        override fun close(): Unit {
            actions.forEach { it.runCatching { invoke() } }
            actions.clear()
            // no real cleanup for ByteBuffers for now
            roots.forEach { it.close() }
            roots.clear()
        }

        override fun invokeOnClose(block: () -> Unit) {
            actions.add(block)
        }
    }

    object Implicit : MemoryArenaImpl() {
        override fun allocate(size: MemoryAddressSize, alignment: MemoryAddressSize): MemorySegment {
            val holder = BufferAllocator.allocate(size)
            return MemorySegment(holder)
        }

        override fun close() {
            TODO("should not be called on implicit scope")
        }

        override fun invokeOnClose(block: () -> Unit) {
            TODO("should not be called on implicit scope")
        }
    }
}
