package dev.whyoleg.foreign.memory

import kotlinx.cinterop.*
import kotlin.native.internal.*

public actual abstract class MemoryScope {
    @ForeignMemoryApi
    public actual abstract fun allocateMemory(layout: MemoryLayout): MemorySegment

    public actual class Closeable actual constructor() : MemoryScope() {
        private val arena = Arena()

        @ForeignMemoryApi
        override fun allocateMemory(layout: MemoryLayout): MemorySegment {
            val ptr = arena.alloc(layout.size, layout.alignment.toInt()).rawPtr
            return MemorySegment(ptr, layout.size, null)
        }

        public actual fun close() {
            arena.clear()
        }
    }

    public actual object Auto : MemoryScope() {
        @ForeignMemoryApi
        override fun allocateMemory(layout: MemoryLayout): MemorySegment {
            val ptr = nativeHeap.alloc(layout.size, layout.alignment.toInt()).rawPtr
            return MemorySegment(ptr, layout.size, createCleaner(ptr, nativeHeap::free))
        }
    }
}
