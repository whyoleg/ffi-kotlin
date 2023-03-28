package dev.whyoleg.foreign.memory

import kotlinx.cinterop.*
import kotlinx.cinterop.NativePtr
import kotlin.native.internal.*

public actual abstract class MemoryScope {
    @ForeignMemoryApi
    protected fun NativePlacement.alloc(layout: MemoryLayout): NativePtr =
        alloc(layout.size, layout.alignment.toInt()).rawPtr

    @ForeignMemoryApi
    public actual abstract fun allocateMemory(layout: MemoryLayout): MemorySegment

    public actual class Closeable internal constructor() : MemoryScope() {
        private val arena = Arena()

        @ForeignMemoryApi
        override fun allocateMemory(layout: MemoryLayout): MemorySegment {
            val ptr = arena.alloc(layout)
            val segment = MemorySegment(ptr, layout.size, null)
            arena.defer { segment.makeInaccessible() }
            return segment
        }

        public actual fun close(): Unit = arena.clear()
    }

    internal object Auto : MemoryScope() {
        @ForeignMemoryApi
        override fun allocateMemory(layout: MemoryLayout): MemorySegment {
            val ptr = nativeHeap.alloc(layout)
            val cleaner = createCleaner(ptr) { nativeHeap.free(it) }
            return MemorySegment(ptr, layout.size, cleaner)
        }
    }
}
