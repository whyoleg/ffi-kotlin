package dev.whyoleg.foreign.memory

import kotlinx.cinterop.*
import kotlinx.cinterop.NativePtr
import kotlin.native.internal.*

@OptIn(ForeignMemoryApi::class)
public actual class MemoryObject private constructor() {
    public actual val autoAllocator: MemoryAllocator = MemoryAllocator {
        val ptr = nativeHeap.alloc(it)
        val cleaner = createCleaner(ptr) { nativeHeap.free(it) }
        MemorySegment(ptr, it.size, cleaner)
    }

    public actual fun createScope(): MemoryScope = Scope()

    public actual companion object {
        public actual val Default: MemoryObject = MemoryObject()
    }

    private class Scope : MemoryScope {
        private val arena = Arena()
        override fun allocateMemory(layout: MemoryLayout): MemorySegment {
            val ptr = arena.alloc(layout)
            val segment = MemorySegment(ptr, layout.size, null)
            arena.defer { segment.makeInaccessible() }
            return segment
        }

        override fun close() = arena.clear()
    }
}

@ForeignMemoryApi
private fun NativePlacement.alloc(layout: MemoryLayout): NativePtr = alloc(layout.size, layout.alignment.toInt()).rawPtr
