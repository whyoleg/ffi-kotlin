package dev.whyoleg.foreign.memory

import java.nio.*

@OptIn(ForeignMemoryApi::class)
public actual class MemoryObject private constructor() {
    public actual val autoAllocator: MemoryAllocator = MemoryAllocator { size, _ ->
        MemorySegment(BufferHolder.Root(ByteBuffer.allocateDirect(size.toInt())))
    }

    public actual fun createScope(): MemoryScope = Scope()

    @ForeignMemoryApi
    public actual fun unsafeMemory(address: MemoryAddress, layout: MemoryLayout): MemorySegment? =
        MemorySegment.fromAddress(address, layout)

    public actual companion object {
        public actual val Default: MemoryObject = MemoryObject()
    }

    private class Scope : MemoryScope {
        private val roots = mutableListOf<BufferHolder.Root>()
        override fun allocateMemory(size: MemoryAddressSize, alignment: MemoryAddressSize): MemorySegment {
            val holder = BufferHolder.Root(ByteBuffer.allocateDirect(size.toInt()))
            roots.add(holder)
            return MemorySegment(holder)
        }

        override fun close() {
            // no real cleanup for ByteBuffers for now
            roots.forEach { it.makeInaccessible() }
            roots.clear()
        }
    }
}
