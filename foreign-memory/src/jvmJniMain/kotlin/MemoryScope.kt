package dev.whyoleg.foreign.memory

import java.nio.*

public actual abstract class MemoryScope {
    @ForeignMemoryApi
    public actual abstract fun allocateMemory(layout: MemoryLayout): MemorySegment

    public actual class Closeable : MemoryScope() {
        @ForeignMemoryApi
        private val roots = mutableListOf<BufferHolder.Root>()

        @ForeignMemoryApi
        override fun allocateMemory(layout: MemoryLayout): MemorySegment {
            val holder = BufferHolder.Root(ByteBuffer.allocateDirect(layout.size.toInt()))
            roots.add(holder)
            return MemorySegment(holder)
        }

        @OptIn(ForeignMemoryApi::class)
        public actual fun close() {
            // no real cleanup for ByteBuffers for now
            roots.forEach { it.makeInaccessible() }
            roots.clear()
        }
    }

    internal object Auto : MemoryScope() {
        @ForeignMemoryApi
        override fun allocateMemory(layout: MemoryLayout): MemorySegment {
            return MemorySegment(BufferHolder.Root(ByteBuffer.allocateDirect(layout.size.toInt())))
        }
    }
}
