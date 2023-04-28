package dev.whyoleg.foreign.nio

import dev.whyoleg.foreign.memory.*

@ForeignMemoryApi
internal sealed class ByteBufferMemoryArena : MemoryArena() {
    class Shared : ByteBufferMemoryArena() {
        //TODO: cleaner support?
        private val roots = mutableListOf<BufferHolder.Root>()
        private val actions = mutableListOf<() -> Unit>()
        override fun allocate(size: MemoryAddressSize, alignment: MemoryAddressSize): MemoryBlock {
            val holder = BufferAllocator.allocate(size)
            roots.add(holder)
            return ByteBufferMemoryBlock(holder)
        }

        override fun wrap(address: MemoryAddressSize, layout: MemoryBlockLayout): MemoryBlock? {
            return ByteBufferMemoryBlock.fromAddress(address, layout.size, roots::add)
        }

        override fun close() {
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

    object Implicit : ByteBufferMemoryArena() {
        override fun allocate(size: MemoryAddressSize, alignment: MemoryAddressSize): MemoryBlock {
            val holder = BufferAllocator.allocate(size)
            return ByteBufferMemoryBlock(holder)
        }

        override fun wrap(address: MemoryAddressSize, layout: MemoryBlockLayout): MemoryBlock? {
            return ByteBufferMemoryBlock.fromAddress(address, layout.size)
        }

        override fun close() {
            TODO("should not be called on implicit scope")
        }

        override fun invokeOnClose(block: () -> Unit) {
            TODO("should not be called on implicit scope")
        }
    }
}
