package dev.whyoleg.foreign.memory

import java.nio.*

@OptIn(ForeignMemoryApi::class)
public actual class MemoryObject private constructor() {
    public actual val autoAllocator: MemoryAllocator = MemoryAllocator {
        MemorySegment(BufferHolder.Root(ByteBuffer.allocateDirect(it.size.toInt())))
    }

    public actual fun createScope(): MemoryScope = Scope()

    public actual companion object {
        public actual val Default: MemoryObject = MemoryObject()
    }

    private class Scope : MemoryScope {
        private val roots = mutableListOf<BufferHolder.Root>()
        override fun allocateMemory(layout: MemoryLayout): MemorySegment {
            val holder = BufferHolder.Root(ByteBuffer.allocateDirect(layout.size.toInt()))
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
