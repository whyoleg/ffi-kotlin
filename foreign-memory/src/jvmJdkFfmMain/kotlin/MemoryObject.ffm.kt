package dev.whyoleg.foreign.memory

import java.lang.foreign.*

@OptIn(ForeignMemoryApi::class)
public actual class MemoryObject private constructor() {
    public actual val autoAllocator: MemoryAllocator = MemoryAllocator { SegmentScope.auto().alloc(it) }
    public actual fun createScope(): MemoryScope = Scope()

    public actual companion object {
        public actual val Default: MemoryObject = MemoryObject()
    }

    private class Scope : MemoryScope {
        //TODO: confined vs shared
        private val arena = Arena.openShared()
        override fun allocateMemory(layout: MemoryLayout): MemorySegment = arena.scope().alloc(layout)
        override fun close() = arena.close()
    }
}

@ForeignMemoryApi
private fun SegmentScope.alloc(layout: MemoryLayout): MemorySegment {
    return MemorySegment(java.lang.foreign.MemorySegment.allocateNative(layout.size, layout.alignment, this))
}
